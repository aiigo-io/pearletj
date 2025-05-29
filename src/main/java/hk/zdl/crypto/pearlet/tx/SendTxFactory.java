package hk.zdl.crypto.pearlet.tx;

import java.math.BigDecimal;

import hk.zdl.crypto.pearlet.ds.CryptoNetwork;

public class SendTxFactory {

	/**
	 * 静态工厂方法，根据网络类型创建具体的SendTx实例
	 * 
	 * @param network 加密网络类型
	 * @param from    发送账户
	 * @param to      接收账户
	 * @param amount  转账金额
	 * @param fee     手续费
	 * @param assetId 资产ID（原生代币时为null）
	 * @return 具体的SendTx实现类实例
	 */
	public static SendTx createSendTx(CryptoNetwork network, String from, String to, BigDecimal amount, BigDecimal fee, String assetId) {
		if (network.isBurst()) {
			return new BurstSendTx.Builder().network(network).from(from).to(to).amount(amount).fee(fee).assetId(assetId).build();
		} else if (network.isWeb3J()) {
			return new Web3jSendTx.Builder().network(network).from(from).to(to).amount(amount).fee(fee).assetId(assetId).build();
		}
		throw new IllegalArgumentException("不支持的网络类型: " + network.getName());
	}
}