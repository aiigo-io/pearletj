package hk.zdl.crypto.pearlet.component.account_settings.web3j;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.DynamicBytes;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Hash;
import org.web3j.crypto.Sign;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.gas.DynamicGasProvider;
import org.web3j.utils.Numeric;

import hk.zdl.crypto.pearlet.util.CryptoUtil;

public class MultiSigWorker implements Callable<TransactionReceipt> {

	/** Gnosis Safe 合约地址（主网默认地址：0x68b3465833fb72A70ecDF485Ee4263c06Fd50fda） **/
	private static final String safeAddress = "0x68b3465833fb72A70ecDF485Ee4263c06Fd50fda";

	private final byte[] private_key;
	private final List<String> owners;
	private final BigInteger threshold;

	protected MultiSigWorker(byte[] private_key, List<String> owners, BigInteger threshold) {
		this.private_key = private_key;
		this.owners = owners;
		this.threshold = threshold;
	}

	@Override
	public TransactionReceipt call() throws Exception {
		// 1. 获取Web3j实例和链ID
		Web3j web3j = CryptoUtil.getWeb3j().get();
		Credentials credentials = Credentials.create(ECKeyPair.create(private_key));
		long chainId = web3j.ethChainId().send().getChainId().longValue();

		// 2. 创建动态Gas提供者
		DynamicGasProvider gasProvider = new DynamicGasProvider(web3j);

		// 3. 构建Safe合约的setup方法调用（使用成员变量owners和threshold）
		Function setupFunction = new Function("setup", Arrays.asList(new DynamicArray<>(Address.class, owners.stream().map(Address::new).collect(Collectors.toList())), new Uint256(threshold),
				Address.DEFAULT, new DynamicBytes(new byte[] {}), Address.DEFAULT, Address.DEFAULT, new Uint256(BigInteger.ZERO), Address.DEFAULT), Collections.emptyList());

		// 4. 编码方法调用数据
		String encodedFunction = FunctionEncoder.encode(setupFunction);

		// 5. 获取Safe nonce
		BigInteger nonce = web3j.ethGetTransactionCount(safeAddress, DefaultBlockParameterName.PENDING).send().getTransactionCount();

		// 6. 计算域分隔符和交易哈希
		Bytes32 domainSeparator = calculateDomainSeparator(chainId, safeAddress);
		Bytes32 safeTxHash = calculateSafeTxHash(safeAddress, BigInteger.ZERO, encodedFunction, 0, BigInteger.ZERO, BigInteger.ZERO, gasProvider.getGasPrice(), Address.DEFAULT.toString(),
				Address.DEFAULT.toString(), nonce, domainSeparator);

		// 7. 签名交易哈希
		Sign.SignatureData signature = Sign.signMessage(safeTxHash.getValue(), credentials.getEcKeyPair(), false);
		String signatureHex = Numeric.toHexStringNoPrefix(signature.getR()) + Numeric.toHexStringNoPrefix(signature.getS())
				+ String.format("%02x", 27 + Numeric.toBigInt(signature.getV()).intValueExact() + (int) (chainId * 2 + 8));

		// 8. 构建execTransaction方法调用
		Function execFunction = new Function("execTransaction",
				Arrays.asList(new Address(safeAddress), new Uint256(BigInteger.ZERO), new DynamicBytes(Numeric.hexStringToByteArray(encodedFunction)), new Uint8(0), new Uint256(BigInteger.ZERO),
						new Uint256(BigInteger.ZERO), new Uint256(gasProvider.getGasPrice()), Address.DEFAULT, Address.DEFAULT, new DynamicBytes(Numeric.hexStringToByteArray(signatureHex))),
				Collections.singletonList(new TypeReference<Bool>() {
				}));

		// 9. 发送交易并获取收据
		RawTransactionManager txManager = new RawTransactionManager(web3j, credentials, chainId);
		EthSendTransaction response = txManager.sendTransaction(gasProvider.getGasPrice(), gasProvider.getGasLimit(), safeAddress, FunctionEncoder.encode(execFunction), BigInteger.ZERO);

		if (response.hasError()) {
			throw new TransactionException(response.getError().getMessage(), response.getTransactionHash());
		}

		// 10. 等待交易确认并返回收据
		return web3j.ethGetTransactionReceipt(response.getTransactionHash()).send().getTransactionReceipt().orElseThrow(() -> new TransactionException("", response.getTransactionHash()));
	}

	// 计算域分隔符（Domain Separator）
	@SuppressWarnings({ "rawtypes", "unused" })
	private static Bytes32 calculateDomainSeparator(long chainId, String verifyingContract) {
		// 1. 计算域类型哈希
		String domainType = "EIP712Domain(uint256 chainId,address verifyingContract)";
		Bytes32 domainTypeHash = keccak256(domainType.getBytes(StandardCharsets.UTF_8));

		// 2. 编码域值
		var domainValues = Arrays.<Type>asList(new Uint256(chainId), new Address(verifyingContract));

		// 3. 计算域分隔符
		String encodedDomain = FunctionEncoder.encodeConstructor(domainValues);
		return keccak256(Numeric.hexStringToByteArray(encodedDomain));
	}

	// 计算 Safe 交易哈希
	@SuppressWarnings({ "rawtypes", "unused" })
	private static Bytes32 calculateSafeTxHash(String to, BigInteger value, String data, int operation, BigInteger safeTxGas, BigInteger baseGas, BigInteger gasPrice, String gasToken,
			String refundReceiver, BigInteger nonce, Bytes32 domainSeparator) {
		// 1. 计算 SafeTx 类型哈希
		String safeTxType = "SafeTx(address to,uint256 value,bytes data,uint8 operation,uint256 safeTxGas,uint256 baseGas,uint256 gasPrice,address gasToken,address refundReceiver,uint256 nonce)";
		Bytes32 safeTxTypeHash = keccak256(safeTxType.getBytes(StandardCharsets.UTF_8));

		// 2. 编码交易数据
		var txValues = Arrays.<Type>asList(new Address(to), new Uint256(value), new DynamicBytes(Numeric.hexStringToByteArray(data)), new Uint8(operation), new Uint256(safeTxGas),
				new Uint256(baseGas), new Uint256(gasPrice), new Address(gasToken), new Address(refundReceiver), new Uint256(nonce));

		// 3. 计算交易数据哈希
		String encodedTx = FunctionEncoder.encodeConstructor(txValues);
		Bytes32 txDataHash = keccak256(Numeric.hexStringToByteArray(encodedTx));

		// 4. 计算最终 SafeTxHash（结合域分隔符）
		byte[] dataToSign = new byte[66];
		dataToSign[0] = (byte) 0x19;
		dataToSign[1] = (byte) 0x01;
		System.arraycopy(domainSeparator.getValue(), 0, dataToSign, 2, 32);
		System.arraycopy(txDataHash.getValue(), 0, dataToSign, 34, 32);

		return keccak256(dataToSign);
	}

	// 计算 Keccak-256 哈希
	private static Bytes32 keccak256(byte[] data) {
		return new Bytes32(Hash.sha3(data));
	}
}
