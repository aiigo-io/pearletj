package hk.zdl.crypto.pearlet.tx;

import java.math.BigDecimal;
import java.util.concurrent.Callable;

import hk.zdl.crypto.pearlet.ds.CryptoNetwork;

public abstract class SendTx implements Callable<Boolean> {

	protected final CryptoNetwork network;
	protected final String from, to;
	protected final BigDecimal amount, fee;
	protected boolean isEncrypted = false;
	protected byte[] bin_message;
	protected String str_message;
	protected String asset_id;

	public SendTx(CryptoNetwork network, String from, String to, BigDecimal amount, BigDecimal fee, String asset_id) {
		this.network = network;
		this.from = from;
		this.to = to;
		this.amount = amount;
		this.fee = fee;
		this.asset_id = asset_id;
	}

	public void setEncrypted(boolean b) {
		isEncrypted = b;
	}

	public void setMessage(byte[] b) {
		bin_message = b;
	}

	public void setMessage(String str) {
		str_message = str;
	}
}
