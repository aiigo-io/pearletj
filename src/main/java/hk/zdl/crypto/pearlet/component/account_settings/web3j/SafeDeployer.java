package hk.zdl.crypto.pearlet.component.account_settings.web3j;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.DynamicBytes;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.gas.DynamicGasProvider;
import org.web3j.utils.Numeric;

import hk.zdl.crypto.pearlet.util.CryptoUtil;
import hk.zdl.crypto.pearlet.util.Util;

/**
 * Safe 协议部署器，负责通过代理工厂合约部署新的 Safe 实例
 */
public class SafeDeployer implements Callable<TransactionReceipt> {

	// Safe 主合约地址（不同网络可能不同）
	private static final String SAFE_MASTER_COPY = "0x68b3465833fb72A70ecDF485Ee4263c06Fd50fda";
	// Safe 代理工厂合约地址
	private static final String SAFE_PROXY_FACTORY = "0xa6B71E26C5e0845f74c812102Ca7114b6a896AB2";

	private final byte[] deployerPrivateKey;
	private final List<String> owners;
	private final BigInteger threshold;

	public SafeDeployer(byte[] deployerPrivateKey, List<String> owners, BigInteger threshold) {
		this.deployerPrivateKey = deployerPrivateKey;
		this.owners = owners;
		this.threshold = threshold;
	}

	@Override
	public TransactionReceipt call() throws Exception {
		// 1. 初始化 Web3j 和凭证
		var web3j = CryptoUtil.getWeb3j().get();
		var deployerCredentials = Credentials.create(ECKeyPair.create(deployerPrivateKey));
		var chainId = web3j.ethChainId().send().getChainId().longValue();

		// 2. 创建动态 Gas 提供者
		var gasProvider = new DynamicGasProvider(web3j);

		// 3. 构建 Safe 初始化数据（setup 函数调用）
		@SuppressWarnings("rawtypes")
		List<Type> setupParams = new ArrayList<>();
		setupParams.add(new DynamicArray<>(Address.class, owners.stream().map(Address::new).collect(Collectors.toList())));
		setupParams.add(new Uint256(threshold));
		setupParams.add(new Address("0x0000000000000000000000000000000000000000"));
		setupParams.add(new DynamicBytes(Numeric.hexStringToByteArray("0x"))); // Wrap bytes in DynamicBytes
		setupParams.add(new Address("0x0000000000000000000000000000000000000000"));
		setupParams.add(new Address("0x0000000000000000000000000000000000000000"));
		setupParams.add(new Uint256(BigInteger.ZERO)); // Wrap BigInteger in Uint256
		setupParams.add(new Address("0x0000000000000000000000000000000000000000"));

		Function setupFunction = new Function("setup", setupParams, // Now explicitly a List<Type>
				Collections.emptyList());
		String initializer = FunctionEncoder.encode(setupFunction);

		// 4. 构建代理工厂 createProxy 函数调用
		Function createProxyFunction = new Function("createProxy", Arrays.asList(new Address(SAFE_MASTER_COPY), new DynamicBytes(Numeric.hexStringToByteArray(initializer))),
				Collections.singletonList(new TypeReference<Address>() {
				}));

		// 5. 发送部署交易
		RawTransactionManager txManager = new RawTransactionManager(web3j, deployerCredentials, chainId);
		EthSendTransaction response = txManager.sendTransaction(gasProvider.getGasPrice(), gasProvider.getGasLimit(), SAFE_PROXY_FACTORY, FunctionEncoder.encode(createProxyFunction), BigInteger.ZERO);

		// 6. 处理部署结果
		if (response.hasError()) {
			throw new TransactionException(Util.getResourceBundle().getString("ERROR.DEPLOY_FAILED") + ": " + response.getError().getMessage(), response.getTransactionHash());
		}

		// 7. 等待交易确认并返回收据
		return web3j.ethGetTransactionReceipt(response.getTransactionHash()).send().getTransactionReceipt().get();
	}
}