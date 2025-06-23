import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.DynamicBytes;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Hash;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Sign;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.gas.DynamicGasProvider;
import org.web3j.utils.Numeric;

public class SafeTransactionExample {
	public static void main(String[] args) throws Exception {
		// 1. 连接以太坊网络
		Web3j web3j = Web3j.build(new HttpService("https://testnet.aiigo.org"));
		// 生成新的密钥对
        ECKeyPair keyPair = Keys.createEcKeyPair();
        BigInteger privateKey = keyPair.getPrivateKey();
        
        // 转换为十六进制字符串（私钥）
        String privateKeyHex = "0x" + privateKey.toString(16);
		Credentials credentials = Credentials.create(privateKeyHex);
		long chainId = web3j.ethChainId().send().getChainId().longValue();

		// 2. 创建动态 Gas 提供者（从网络获取当前 Gas 价格）
		DynamicGasProvider gasProvider = new DynamicGasProvider(web3j);

		// 3. Safe 合约和交易参数
		String safeAddress = "0x68b3465833fb72A70ecDF485Ee4263c06Fd50fda";
		String toAddress = "0x28265d2be88fc45ba88fe60df39d7c5b8289aa24";
		String data = "0x"; // 若无则填0x
		BigInteger value = BigInteger.ZERO;
		int operation = 0; // 0=CALL, 1=DELEGATE_CALL
		BigInteger safeTxGas = BigInteger.valueOf(100000);
		BigInteger baseGas = BigInteger.ZERO;
		String gasToken = "0x00000000000000000000000000000000000000";
		String refundReceiver = "0x00000000000000000000000000000000000000";

		// 获取 Safe nonce
		BigInteger nonce = web3j.ethGetTransactionCount(safeAddress, DefaultBlockParameterName.PENDING).send().getTransactionCount();

		// 4. 计算域分隔符（Domain Separator）
		Bytes32 domainSeparator = calculateDomainSeparator(chainId, safeAddress);

		// 5. 计算交易哈希（SafeTxHash）
		Bytes32 safeTxHash = calculateSafeTxHash(toAddress, value, data, operation, safeTxGas, baseGas, gasProvider.getGasPrice(), gasToken, refundReceiver, nonce, domainSeparator);

		// 6. 签名交易哈希
		Sign.SignatureData signature = Sign.signMessage(safeTxHash.getValue(), credentials.getEcKeyPair(), false);

		// 7. 处理签名结果
		String r = Numeric.toHexStringNoPrefix(signature.getR());
		String s = Numeric.toHexStringNoPrefix(signature.getS());
		int v = Numeric.toBigInt(signature.getV()).intValueExact();
		v = 27 + v + (int) (chainId * 2 + 8); // 应用 EIP-155 偏移
		String signatureHex = r + s + String.format("%02x", v);

		System.out.println("SafeTxHash: " + Numeric.toHexString(safeTxHash.getValue()));
		System.out.println("签名结果: " + signatureHex);

		// 8. 构建 Safe 合约的 execTransaction 方法调用
		Function function = new Function("execTransaction",
				Arrays.asList(new Address(toAddress), new Uint256(value), new DynamicBytes(Numeric.hexStringToByteArray(data)), new Uint8(operation), new Uint256(safeTxGas), new Uint256(baseGas),
						new Uint256(gasProvider.getGasPrice()), new Address(gasToken), new Address(refundReceiver), new DynamicBytes(Numeric.hexStringToByteArray(signatureHex))),
				Collections.singletonList(new TypeReference<Bool>() {
				}));

		// 9. 编码方法调用数据
		String encodedFunction = FunctionEncoder.encode(function);

		// 10. 使用动态 Gas 提供者发送交易
		RawTransactionManager txManager = new RawTransactionManager(web3j, credentials, chainId);
		EthSendTransaction response = txManager.sendTransaction(gasProvider.getGasPrice(), gasProvider.getGasLimit(), safeAddress, encodedFunction, BigInteger.ZERO);

		// 11. 处理交易结果
		if (response.hasError()) {
			System.out.println("交易失败: " + response.getError().getMessage());
		} else {
			System.out.println("交易已发送，哈希: " + response.getTransactionHash());
			System.out.println("Gas Price: " + gasProvider.getGasPrice());
			System.out.println("Gas Limit: " + gasProvider.getGasLimit());
		}
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