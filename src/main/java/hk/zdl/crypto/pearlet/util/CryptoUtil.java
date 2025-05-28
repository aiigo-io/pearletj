package hk.zdl.crypto.pearlet.util;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.bouncycastle.util.encoders.Base64;
import org.bouncycastle.util.encoders.Hex;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.web3j.contracts.eip20.generated.ERC20;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;

import hk.zdl.crypto.pearlet.component.account_settings.burst.PKT;
import hk.zdl.crypto.pearlet.ds.CryptoNetwork;
import hk.zdl.crypto.pearlet.ds.RoturaAddress;
import hk.zdl.crypto.pearlet.persistence.MyDb;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import signumj.crypto.SignumCrypto;
import signumj.entity.EncryptedMessage;
import signumj.entity.SignumAddress;
import signumj.entity.SignumID;
import signumj.entity.SignumValue;
import signumj.entity.response.Account;
import signumj.entity.response.Asset;
import signumj.entity.response.AssetBalance;
import signumj.entity.response.FeeSuggestion;
import signumj.entity.response.Transaction;
import signumj.entity.response.http.BRSError;
import signumj.service.NodeService;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.crypto.Credentials;

public class CryptoUtil {

	public static final int peth_decimals = 4;

	private static final OkHttpClient _client = new OkHttpClient();
	private static final Map<CryptoNetwork, JSONObject> nwc_cache = new HashMap<>();
	private static final Map<CryptoNetwork, FeeSuggestion> fee_cache = new HashMap<>();
	private static Web3j _web3j;

	private static final synchronized void build_web3j() {
		var o_1 = MyDb.get_networks().stream().filter(o -> o.isWeb3J()).findAny();
		var o_2 = MyDb.get_webj_auth();
		if (o_1.isPresent()) {
			String base_url = o_1.get().getUrl();
			if (!base_url.endsWith("/")) {
				base_url += "/";
			}
			var httpCredentials = okhttp3.Credentials.basic("", o_2.get().getStr("SECRET"));
			var client = new OkHttpClient.Builder().addInterceptor(chain -> {
				Request request = chain.request();
				Request authenticatedRequest = request.newBuilder().header("Authorization", httpCredentials).build();
				return chain.proceed(authenticatedRequest);
			}).build();
			_web3j = Web3j.build(new HttpService(base_url, client));
		}
	}

	static {
		RoturaAddress.getAddressPrefix();
	}

	public static final void clear_web3j() {
		_web3j = null;
	}

	public static final Optional<Web3j> getWeb3j() {
		if (_web3j == null) {
			build_web3j();
		}
		return _web3j == null ? Optional.empty() : Optional.of(_web3j);
	}

	public static final boolean isValidAddress(CryptoNetwork network, String address) {
		if (address == null || address.isBlank()) {
			return false;
		}
		if (network.isBurst()) {
			try {
				return SignumAddress.fromEither(address) != null;
			} catch (Exception e) {
				return false;
			}
		} else if (network.isWeb3J()) {
			return address.contains(".") || WalletUtils.isValidAddress(address);
		}
		return false;
	}

	public static final byte[] getPublicKeyFromAddress(CryptoNetwork network, String addr) {
		if (network.isBurst()) {
			return SignumAddress.fromRs(addr).getPublicKey();
		}
		throw new UnsupportedOperationException();
	}

	public static final byte[] getPublicKey(CryptoNetwork network, String type, String text) {
		if (network.isBurst()) {
			if (type.equalsIgnoreCase("phrase")) {
				return SignumCrypto.getInstance().getPublicKey(text);
			}
		}
		throw new UnsupportedOperationException();
	}

	public static final byte[] getPrivateKey(CryptoNetwork nw, PKT type, String text) {
		switch (type) {
		case Base64:
			return Base64.decode(text);
		case HEX:
			return Hex.decode(text);
		case Phrase:
			if (nw.isBurst()) {
				return SignumCrypto.getInstance().getPrivateKey(text);
			}
		}
		throw new UnsupportedOperationException();
	}

	public static final byte[] getPublicKey(CryptoNetwork nw, byte[] private_key) {
		if (nw.isBurst()) {
			return SignumCrypto.getInstance().getPublicKey(private_key);
		}
		throw new UnsupportedOperationException();
	}

	public static final String getAddress(CryptoNetwork nw, byte[] public_key) {
		if (nw.isWeb3J()) {
			return "0x" + org.web3j.crypto.Keys.getAddress(org.web3j.utils.Numeric.toBigInt(public_key));
		} else if (nw.isBurst()) {
			try {
				var raw = SignumCrypto.getInstance().getAddressFromPublic(public_key).getRawAddress();
				var addressPrefix = CryptoUtil.getConstants(nw).get("addressPrefix");
				return addressPrefix + "-" + raw;
			} catch (Exception e) {
			}
		}
		return null;
	}

	public static final Asset getAsset(CryptoNetwork network, String assetId) {
		var ns = NodeService.getInstance(network.getUrl());
		return ns.getAsset(SignumID.fromLong(assetId)).blockingGet();
	}

	public static final byte[] addCommitment(CryptoNetwork network, byte[] public_key, BigDecimal amount, BigDecimal fee) throws Exception {
		if (network.isBurst()) {
			var server_url = network.getUrl();
			if (!server_url.endsWith("/")) {
				server_url += "/";
			}
			var httpPost = new HttpPost(server_url + "burst?requestType=addCommitment");
			var l = new LinkedList<NameValuePair>();
			l.add(new BasicNameValuePair("deadline", "1440"));
			l.add(new BasicNameValuePair("broadcast", "false"));
			l.add(new BasicNameValuePair("amountNQT", toSignumValue(network, amount).toNQT().toString()));
			l.add(new BasicNameValuePair("feeNQT", toSignumValue(network, fee).toNQT().toString()));
			l.add(new BasicNameValuePair("publicKey", Hex.toHexString(public_key)));
			httpPost.setEntity(new UrlEncodedFormEntity(l));
			var httpclient = WebUtil.getHttpclient();
			var response = httpclient.execute(httpPost);
			var bis = response.getEntity().getContent();
			try {
				var jobj = new JSONObject(new JSONTokener(bis));
				bis.close();
				if (jobj.has("errorDescription")) {
					throw new Exception(jobj.getString("errorDescription"));
				}
				byte[] bArr = Hex.decode(jobj.getString("unsignedTransactionBytes"));
				return bArr;
			} finally {
				bis.close();
			}
		}
		throw new UnsupportedOperationException();
	}

	public static final byte[] removeCommitment(CryptoNetwork network, byte[] public_key, BigDecimal amount, BigDecimal fee) throws Exception {
		if (network.isBurst()) {
			var server_url = network.getUrl();
			if (!server_url.endsWith("/")) {
				server_url += "/";
			}
			var httpPost = new HttpPost(server_url + "burst?requestType=removeCommitment");
			var l = new LinkedList<NameValuePair>();
			l.add(new BasicNameValuePair("deadline", "1440"));
			l.add(new BasicNameValuePair("broadcast", "false"));
			l.add(new BasicNameValuePair("amountNQT", toSignumValue(network, amount).toNQT().toString()));
			l.add(new BasicNameValuePair("feeNQT", toSignumValue(network, fee).toNQT().toString()));
			l.add(new BasicNameValuePair("publicKey", Hex.toHexString(public_key)));
			httpPost.setEntity(new UrlEncodedFormEntity(l));
			var httpclient = WebUtil.getHttpclient();
			var response = httpclient.execute(httpPost);
			var bis = response.getEntity().getContent();
			try {
				var jobj = new JSONObject(new JSONTokener(bis));
				bis.close();
				if (jobj.has("errorDescription")) {
					throw new Exception(jobj.getString("errorDescription"));
				}
				byte[] bArr = Hex.decode(jobj.getString("unsignedTransactionBytes"));
				return bArr;
			} finally {
				bis.close();
			}
		}
		throw new UnsupportedOperationException();
	}

	public static final Account getAccount(CryptoNetwork network, String address) throws Exception {
		var ns = NodeService.getInstance(network.getUrl());
		try {
			return ns.getAccount(SignumAddress.fromEither(address), null, false, true).toFuture().get();
		} catch (Exception e) {
			if (e.getCause().getClass().equals(signumj.entity.response.http.BRSError.class)) {
				if (((BRSError) e.getCause()).getCode() == 5) {
					return new Account(SignumAddress.fromEither(address), SignumValue.ZERO, SignumValue.ZERO, SignumValue.ZERO, SignumValue.ZERO, SignumValue.ZERO, new byte[] {}, "", "",
							new AssetBalance[] {});
				}
			}
			throw e;
		}
	}

	public static final BigDecimal getBalance(CryptoNetwork network, String address) throws Exception {
		if (network.isBurst()) {
			var ns = NodeService.getInstance(network.getUrl());
			try {
				var account = ns.getAccount(SignumAddress.fromEither(address), null, false, true).toFuture().get();
				var balance = account.getBalance();
				var committed_balance = account.getCommittedBalance();
				balance = balance.subtract(committed_balance);
				return new BigDecimal(balance.toNQT(), CryptoUtil.getConstants(network).getInt("decimalPlaces"));
			} catch (Exception e) {
				if (e.getCause() != null) {
					if (e.getCause().getClass().getName().equals("signumj.entity.response.http.BRSError")) {
						BRSError e1 = (BRSError) e.getCause();
						if (e1.getCode() == 5) {
							return new BigDecimal(0);
						}
					}
				}
				throw e;
			}
		} else if (network.isWeb3J()) {
			if (getWeb3j().isPresent()) {
				try {
					BigInteger wei = getWeb3j().get().ethGetBalance(address, DefaultBlockParameterName.LATEST).send().getBalance();
					BigDecimal eth = Convert.fromWei(new BigDecimal(wei), Convert.Unit.ETHER);
					return eth;
				} catch (IOException e) {
				}
			}
			var items = getAccountBalances(address);
			for (int i = 0; i < items.length(); i++) {
				var jobj = items.getJSONObject(i);
				if (jobj.getString("contract_name").equals("Ether") && jobj.getString("contract_ticker_symbol").equals("ETH")) {
					BigInteger wei = new BigInteger(jobj.getString("balance"));
					BigDecimal eth = Convert.fromWei(new BigDecimal(wei), Convert.Unit.ETHER);
					return eth;
				}
			}
		}
		return new BigDecimal("-1");
	}

	public static JSONArray getAccountBalances(String address) throws Exception {
		var web3j = getWeb3j().orElseThrow(() -> new IOException("Web3j instance not available"));
		BigInteger weiBalance = web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST).send().getBalance();
		JSONObject ethBalance = new JSONObject().put("contract_name", "Ether").put("contract_ticker_symbol", "ETH").put("contract_decimals", 18)
				.put("contract_address", "0x0000000000000000000000000000000000000000") // 添加合约地址字段
				.put("balance", weiBalance.toString());
		return new JSONArray().put(ethBalance);
	}

	public static JSONArray getTxHistory(String address, int page_number, int page_size) throws Exception {
		var client = WebUtil.getHttpclient();
		var url = "http://explorer.aiigo.org/api/v2/addresses/" + address + "/transactions?page=" + page_number + "&page_size=" + page_size;
		var httpGet = new HttpGet(url);

		try (var response = client.execute(httpGet)) {
			var entity = response.getEntity();
			if (response.getStatusLine().getStatusCode() != 200) {
				throw new IOException("HTTP error: " + response.getStatusLine());
			}

			var json = new JSONObject(new JSONTokener(entity.getContent()));
			return json.getJSONArray("items");

		} finally {
			httpGet.releaseConnection();
		}
	}

	public static byte[] generateTransferAssetTransaction(CryptoNetwork nw, byte[] senderPublicKey, String recipient, String assetId, BigDecimal quantity, BigDecimal fee) throws Exception {
		if (nw.isBurst()) {
			var server_url = nw.getUrl();
			if (!server_url.endsWith("/")) {
				server_url += "/";
			}
			var client = new OkHttpClient.Builder().build();
			var request = new Request.Builder().url(server_url + "burst?requestType=transferAsset")
					.post(RequestBody.create("asset=" + assetId + "&recipient=" + recipient + "&deadline=1440&quantityQNT=" + quantity.toPlainString() + "&broadcast=false&feeNQT="
							+ toSignumValue(nw, fee).toNQT() + "&publicKey=" + Hex.toHexString(senderPublicKey), MediaType.parse("application/x-www-form-urlencoded")))
					.build();
			var response = client.newCall(request).execute();
			try {
				var jobj = new JSONObject(new JSONTokener(response.body().charStream()));
				byte[] bArr = Hex.decode(jobj.getString("unsignedTransactionBytes"));
				return bArr;
			} finally {
				response.body().close();
				response.close();
			}
		}
		throw new UnsupportedOperationException();
	};

	public static byte[] generateTransferAssetTransactionWithMessage(CryptoNetwork nw, byte[] senderPublicKey, String recipient, String assetId, BigDecimal quantity, BigDecimal fee, byte[] message)
			throws Exception {
		if (nw.isBurst()) {
			var server_url = nw.getUrl();
			if (!server_url.endsWith("/")) {
				server_url += "/";
			}
			var ns = NodeService.getInstance(server_url);
			return ns.generateTransferAssetTransactionWithMessage(senderPublicKey, SignumAddress.fromEither(recipient), SignumID.fromLong(assetId),
					SignumValue.fromNQT(new BigInteger(quantity.toPlainString())), SignumValue.ZERO, toSignumValue(nw, fee), 1440, message).blockingGet();
		}
		throw new UnsupportedOperationException();
	}

	public static byte[] generateTransferAssetTransactionWithMessage(CryptoNetwork nw, byte[] senderPublicKey, String recipient, String assetId, BigDecimal quantity, BigDecimal fee, String message)
			throws Exception {
		if (nw.isBurst()) {
			var server_url = nw.getUrl();
			if (!server_url.endsWith("/")) {
				server_url += "/";
			}
			var client = new OkHttpClient.Builder().build();
			var request = new Request.Builder().url(server_url + "burst?requestType=transferAsset")
					.post(RequestBody.create(
							"asset=" + assetId + "&recipient=" + recipient + "&deadline=1440&quantityQNT=" + quantity.toPlainString() + "&broadcast=false&feeNQT=" + toSignumValue(nw, fee).toNQT()
									+ "&publicKey=" + Hex.toHexString(senderPublicKey) + "&messageIsText=true&message=" + URLEncoder.encode(message, "UTF-8"),
							MediaType.parse("application/x-www-form-urlencoded")))
					.build();
			var response = client.newCall(request).execute();
			try {
				var jobj = new JSONObject(new JSONTokener(response.body().charStream()));
				byte[] bArr = Hex.decode(jobj.getString("unsignedTransactionBytes"));
				return bArr;
			} finally {
				response.body().close();
				response.close();
			}
		}

		throw new UnsupportedOperationException();
	}

	public static byte[] generateTransaction(CryptoNetwork nw, String recipient, byte[] public_key, BigDecimal amount, BigDecimal fee) throws Exception {
		if (nw.isBurst()) {
			var server_url = nw.getUrl();
			if (!server_url.endsWith("/")) {
				server_url += "/";
			}
			var ns = NodeService.getInstance(server_url);
			return ns.generateTransaction(SignumAddress.fromEither(recipient), public_key, toSignumValue(nw, amount), toSignumValue(nw, fee), 1440, null).blockingGet();
		}
		throw new UnsupportedOperationException();
	}

	public static byte[] generateTransferAssetTransactionWithEncryptedMessage(CryptoNetwork nw, byte[] senderPublicKey, String recipient, String assetId, BigDecimal quantity, BigDecimal fee,
			byte[] message, boolean isText) throws Exception {
		if (nw.isBurst()) {
			var server_url = nw.getUrl();
			if (!server_url.endsWith("/")) {
				server_url += "/";
			}
			var ns = NodeService.getInstance(server_url);
			byte[] nounce = new byte[32];// must be 32
			new Random().nextBytes(nounce);
			EncryptedMessage emsg = new EncryptedMessage(message, nounce, isText);
			return ns.generateTransferAssetTransactionWithEncryptedMessage(senderPublicKey, SignumAddress.fromEither(recipient), SignumID.fromLong(assetId),
					SignumValue.fromNQT(new BigInteger(quantity.toPlainString())), SignumValue.ZERO, toSignumValue(nw, fee), 1440, emsg).blockingGet();
		}
		throw new UnsupportedOperationException();
	}

	public static byte[] generateTransactionWithEncryptedMessage(CryptoNetwork nw, String recipient, byte[] public_key, BigDecimal amount, BigDecimal fee, byte[] message, boolean isText)
			throws Exception {
		if (nw.isBurst()) {
			var server_url = nw.getUrl();
			if (!server_url.endsWith("/")) {
				server_url += "/";
			}
			var ns = NodeService.getInstance(server_url);
			byte[] nounce = new byte[32];// must be 32
			new Random().nextBytes(nounce);
			EncryptedMessage emsg = new EncryptedMessage(message, nounce, isText);
			return ns.generateTransactionWithEncryptedMessage(SignumAddress.fromEither(recipient), public_key, toSignumValue(nw, amount), toSignumValue(nw, fee), 1440, emsg, null).blockingGet();
		}
		throw new UnsupportedOperationException();
	}

	public static byte[] generateTransactionWithMessage(CryptoNetwork nw, String recipient, byte[] public_key, BigDecimal amount, BigDecimal fee, String message) throws Exception {
		if (nw.isBurst()) {
			var server_url = nw.getUrl();
			if (!server_url.endsWith("/")) {
				server_url += "/";
			}
			var ns = NodeService.getInstance(server_url);
			return ns.generateTransactionWithMessage(SignumAddress.fromEither(recipient), public_key, toSignumValue(nw, amount), toSignumValue(nw, fee), 1440, message, null).blockingGet();
		}
		throw new UnsupportedOperationException();
	}

	public static byte[] generateTransactionWithMessage(CryptoNetwork nw, String recipient, byte[] public_key, BigDecimal amount, BigDecimal fee, byte[] message) throws Exception {
		if (nw.isBurst()) {
			var server_url = nw.getUrl();
			if (!server_url.endsWith("/")) {
				server_url += "/";
			}
			var ns = NodeService.getInstance(server_url);
			return ns.generateTransactionWithMessage(SignumAddress.fromEither(recipient), public_key, toSignumValue(nw, amount), toSignumValue(nw, fee), 1440, message, null).blockingGet();
		}
		throw new UnsupportedOperationException();
	}

	public static EncryptedMessage encryptTextMessage(CryptoNetwork nw, byte[] their_public_key, byte[] my_private_key, byte[] message, boolean messageIsText) throws Exception {
		if (nw.isBurst()) {
			EncryptedMessage e_msg;
			if (messageIsText) {
				e_msg = SignumCrypto.getInstance().encryptTextMessage(new String(message), my_private_key, their_public_key);
			} else {
				e_msg = SignumCrypto.getInstance().encryptBytesMessage(message, my_private_key, their_public_key);
			}
			return e_msg;
		}
		throw new UnsupportedOperationException();
	}

	public static byte[] generateTransactionWithEncryptedMessage(CryptoNetwork nw, String recipient, byte[] senderPublicKey, BigDecimal fee, EncryptedMessage message) throws Exception {
		if (nw.isBurst()) {
			var server_url = nw.getUrl();
			if (!server_url.endsWith("/")) {
				server_url += "/";
			}
			var ns = NodeService.getInstance(server_url);
			return ns.generateTransactionWithEncryptedMessage(SignumAddress.fromEither(recipient), senderPublicKey, toSignumValue(nw, fee), 1440, message, "").blockingGet();
		}
		throw new UnsupportedOperationException();
	}

	public static byte[] sendMessage(CryptoNetwork network, String recipient, byte[] public_key, String message, BigDecimal fee) throws Exception {
		if (network.isBurst()) {
			var server_url = network.getUrl();
			if (!server_url.endsWith("/")) {
				server_url += "/";
			}
			var httpPost = new HttpPost(server_url + "burst?requestType=sendMessage");
			var l = new LinkedList<NameValuePair>();
			l.add(new BasicNameValuePair("recipient", recipient));
			l.add(new BasicNameValuePair("message", message));
			l.add(new BasicNameValuePair("deadline", "1440"));
			l.add(new BasicNameValuePair("messageIsText", "true"));
			l.add(new BasicNameValuePair("publicKey", Hex.toHexString(public_key)));
			l.add(new BasicNameValuePair("feeNQT", toSignumValue(network, fee).toNQT().toString()));
			httpPost.setEntity(new UrlEncodedFormEntity(l));
			var httpclient = WebUtil.getHttpclient();
			var response = httpclient.execute(httpPost);
			var bis = response.getEntity().getContent();
			try {
				var jobj = new JSONObject(new JSONTokener(bis));
				bis.close();
				if (jobj.has("errorDescription")) {
					throw new Exception(jobj.getString("errorDescription"));
				}
				byte[] bArr = Hex.decode(jobj.getString("unsignedTransactionBytes"));
				return bArr;
			} finally {
				bis.close();
			}
		}
		throw new UnsupportedOperationException();
	}

	public static byte[] issueAsset(CryptoNetwork nw, String asset_name, String description, long quantityQNT, long feeNQT, byte[] public_key) throws Exception {
		if (nw.isBurst()) {
			var server_url = nw.getUrl();
			if (!server_url.endsWith("/")) {
				server_url += "/";
			}
			var client = new OkHttpClient.Builder().build();
			var request = new Request.Builder().url(server_url + "burst?requestType=issueAsset")
					.post(RequestBody.create(
							"name=" + asset_name + "&description=" + description + "&deadline=1440&quantityQNT=" + quantityQNT + "&feeNQT=" + feeNQT + "&publicKey=" + Hex.toHexString(public_key),
							MediaType.parse("application/x-www-form-urlencoded")))
					.build();
			var response = client.newCall(request).execute();
			try {
				var jobj = new JSONObject(new JSONTokener(response.body().charStream()));
				if (jobj.optInt("errorCode", 0) != 0) {
					throw new IOException(jobj.optString("errorDescription"));
				}
				byte[] bArr = Hex.decode(jobj.getString("unsignedTransactionBytes"));
				return bArr;
			} finally {
				response.body().close();
				response.close();
			}
		}
		throw new UnsupportedOperationException();
	}

	public static byte[] setAccountInfo(CryptoNetwork nw, String name, String description, long feeNQT, byte[] public_key) throws Exception {
		if (nw.isBurst()) {
			if (feeNQT < 2205000L) {
				throw new IllegalArgumentException("not enought fee");
			}
			var server_url = nw.getUrl();
			if (!server_url.endsWith("/")) {
				server_url += "/";
			}
			var client = new OkHttpClient.Builder().build();
			var request = new Request.Builder().url(server_url + "burst?requestType=setAccountInfo")
					.post(RequestBody.create("name=" + name + "&description=" + description + "&deadline=1440&broadcast=false&feeNQT=" + feeNQT + "&publicKey=" + Hex.toHexString(public_key),
							MediaType.parse("application/x-www-form-urlencoded")))
					.build();
			var response = client.newCall(request).execute();
			try {
				var jobj = new JSONObject(new JSONTokener(response.body().charStream()));
				if (jobj.optInt("errorCode", 0) != 0) {
					throw new IOException(jobj.optString("errorDescription"));
				}
				byte[] bArr = Hex.decode(jobj.getString("unsignedTransactionBytes"));
				return bArr;
			} finally {
				response.body().close();
				response.close();
			}
		}
		throw new UnsupportedOperationException();
	}

	public static byte[] signTransaction(CryptoNetwork nw, byte[] privateKey, byte[] unsignedTransaction) {
		if (nw.isBurst()) {
			return SignumCrypto.getInstance().signTransaction(privateKey, unsignedTransaction);
		}
		throw new UnsupportedOperationException();
	}

	public static Object broadcastTransaction(CryptoNetwork nw, byte[] signedTransactionBytes) {
		if (nw.isBurst()) {
			var ns = NodeService.getInstance(nw.getUrl());
			return ns.broadcastTransaction(signedTransactionBytes).blockingGet();
		}
		throw new UnsupportedOperationException();
	}

	public static final JSONObject getSignumBlock(CryptoNetwork nw, String block_id) throws Exception {
		if (nw == null || block_id == null || block_id.isBlank()) {
			throw new IllegalArgumentException();
		} else if (!nw.isBurst()) {
			throw new UnsupportedOperationException();
		}
		var server_url = nw.getUrl();
		if (!server_url.endsWith("/")) {
			server_url += "/";
		}
		var request = new Request.Builder().url(server_url + "burst?requestType=getBlock&block=" + block_id).build();
		var response = _client.newCall(request).execute();
		try {
			var jobj = new JSONObject(new JSONTokener(response.body().charStream()));
			if (jobj.optInt("errorCode") > 0) {
				throw new IOException(jobj.optString("errorDescription"));
			} else {
				return jobj;
			}
		} finally {
			response.body().close();
			response.close();
		}
	}

	public static final JSONArray getSignumBlockID(CryptoNetwork nw, String address, long timestamp) throws Exception {
		if (nw == null || address == null || address.isBlank()) {
			throw new IllegalArgumentException();
		} else if (!nw.isBurst()) {
			throw new UnsupportedOperationException();
		}
		var server_url = nw.getUrl();
		if (!server_url.endsWith("/")) {
			server_url += "/";
		}
		var request = new Request.Builder().url(server_url + "burst?requestType=getAccountBlockIds&account=" + address + "&timestamp=" + timestamp).build();
		var response = _client.newCall(request).execute();
		try {
			var jobj = new JSONObject(new JSONTokener(response.body().charStream()));
			if (jobj.optInt("errorCode") > 0) {
				if (jobj.optInt("errorCode") == 5) {
					return new JSONArray();
				} else {
					throw new IOException(jobj.optString("errorDescription"));
				}
			}
			var items = jobj.getJSONArray("blockIds");
			return items;
		} finally {
			response.body().close();
			response.close();
		}
	}

	public static final JSONArray getSignumBlockID(CryptoNetwork nw, String address, int from, int to) throws Exception {
		if (nw == null || address == null || address.isBlank() || from < 0 || to < 0) {
			throw new IllegalArgumentException();
		} else if (!nw.isBurst()) {
			throw new UnsupportedOperationException();
		}
		var server_url = nw.getUrl();
		if (!server_url.endsWith("/")) {
			server_url += "/";
		}
		var request = new Request.Builder().url(server_url + "burst?requestType=getAccountBlockIds&account=" + address + "&firstIndex=" + from + "&lastIndex=" + to).build();
		var response = _client.newCall(request).execute();
		try {
			var jobj = new JSONObject(new JSONTokener(response.body().charStream()));
			if (jobj.optInt("errorCode") > 0) {
				if (jobj.optInt("errorCode") == 5) {
					return new JSONArray();
				} else {
					throw new IOException(jobj.optString("errorDescription"));
				}
			}
			var items = jobj.getJSONArray("blockIds");
			return items;
		} finally {
			response.body().close();
			response.close();
		}
	}

	public static final JSONArray getSignumTxID(CryptoNetwork nw, String address, int from, int to) throws Exception {
		if (nw == null || address == null || address.isBlank() || from < 0 || to < 0) {
			throw new IllegalArgumentException();
		}
		var server_url = nw.getUrl();
		if (!server_url.endsWith("/")) {
			server_url += "/";
		}
		var request = new Request.Builder().url(server_url + "burst?requestType=getAccountTransactionIds&account=" + address + "&firstIndex=" + from + "&lastIndex=" + to).build();
		var response = _client.newCall(request).execute();
		try {
			var jobj = new JSONObject(new JSONTokener(response.body().charStream()));
			if (jobj.optInt("errorCode") > 0) {
				throw new IOException(jobj.optString("errorDescription"));
			}
			var items = jobj.getJSONArray("transactionIds");
			return items;
		} finally {
			response.body().close();
			response.close();
		}
	}

	public static final JSONObject getSignumTx(CryptoNetwork nw, String tx_id) throws Exception {
		if (nw == null || tx_id == null || tx_id.isBlank()) {
			throw new IllegalArgumentException();
		}
		var server_url = nw.getUrl();
		if (!server_url.endsWith("/")) {
			server_url += "/";
		}
		var request = new Request.Builder().url(server_url + "burst?requestType=getTransaction&transaction=" + tx_id).build();
		var response = _client.newCall(request).execute();
		try {
			var jobj = new JSONObject(new JSONTokener(response.body().charStream()));
			if (jobj.optInt("errorCode") > 0) {
				throw new IOException(jobj.optString("errorDescription"));
			}
			return jobj;
		} finally {
			response.body().close();
			response.close();
		}
	}

	@SuppressWarnings("removal")
	public static final SignumID[] getSignumTxID(CryptoNetwork nw, String address) throws Exception {
		if (address == null || address.isBlank()) {
			return new SignumID[0];
		}
		var server_url = nw.getUrl();
		if (!server_url.endsWith("/")) {
			server_url += "/";
		}
		var ns = NodeService.getInstance(server_url);
		SignumID[] id_arr = new SignumID[] {};
		try {
			id_arr = ns.getAccountTransactionIDs(SignumAddress.fromEither(address)).toFuture().get();
		} catch (IllegalArgumentException | InterruptedException | ExecutionException e) {
			if (e.getCause() != null) {
				if (e.getCause().getClass().getName().equals("signumj.entity.response.http.BRSError")) {
					BRSError e1 = (BRSError) e.getCause();
					if (e1.getCode() == 5) {
						return new SignumID[] {};
					}
				}
			}
			throw e;
		} catch (ThreadDeath e) {
			return new SignumID[] {};
		}
		return id_arr;
	}

	public static final Transaction getSignumTx(CryptoNetwork nw, SignumID id) throws Exception {
		return MyDb.getSignumTxFromLocal(nw, id).orElseGet(() -> MyDb.get_server_url(nw).map(NodeService::getInstance).map(s -> s.getTransaction(id)).get().map(o -> {
			MyDb.putSignumTx(nw, o);
			return o;
		}).blockingGet());
	}

	public static final SignumValue toSignumValue(CryptoNetwork network, BigDecimal amount) throws Exception {
		if (network.isBurst()) {
			return SignumValue.fromNQT(amount.movePointRight(getConstants(network).getInt("decimalPlaces")).toBigInteger());
		}
		throw new UnsupportedOperationException();
	}

	public static final synchronized JSONObject getConstants(CryptoNetwork network) throws Exception {
		if (network.isBurst()) {
			var url = network.getUrl();
			var jarr = Util.get_predefined_networks();
			for (var i = 0; i < jarr.length(); i++) {
				var jobj = jarr.getJSONObject(i);
				if (url.equals(jobj.getString("server url"))) {
					return jobj;
				}
			}
			if (nwc_cache.containsKey(network)) {
				return nwc_cache.get(network);
			} else {
				var httpGet = new HttpGet(url + "/burst?requestType=getConstants");
				var httpclient = WebUtil.getHttpclient();
				var response = httpclient.execute(httpGet);
				var bis = response.getEntity().getContent();
				try {
					var o = new JSONObject(new JSONTokener(bis));
					nwc_cache.put(network, o);
					return o;
				} finally {
					bis.close();
				}

			}
		} else if (network.isWeb3J()) { // 添加Web3J网络支持
			// 以太坊默认小数位数为18（ETH/ERC20通用）
			return new JSONObject().put("decimalPlaces", 18);
		}
		throw new UnsupportedOperationException("Unsupported network type: " + network.getType());
	}

	public static int getTokenDecimals(CryptoNetwork network, String tokenAddress) throws Exception {
		if (network.isWeb3J()) {
			Web3j web3j = getWeb3j().orElseThrow(() -> new IOException("Web3j client not initialized"));
			Credentials credentials = null;
			ERC20 erc20 = ERC20.load(tokenAddress, web3j, credentials, new DefaultGasProvider());
			return erc20.decimals().send().intValue();
		}
		throw new UnsupportedOperationException("Unsupported network type: " + network.getType());
	}

	public static final synchronized JSONObject getBlockchainStatus(CryptoNetwork network) throws Exception {
		if (network.isBurst()) {
			var url = network.getUrl();
			var httpGet = new HttpGet(url + "/burst?requestType=getBlockchainStatus");
			var httpclient = WebUtil.getHttpclient();
			var response = httpclient.execute(httpGet);
			var bis = response.getEntity().getContent();
			try {
				return new JSONObject(new JSONTokener(bis));
			} finally {
				bis.close();
			}
		}
		throw new UnsupportedOperationException();
	}

	public static final synchronized FeeSuggestion getFeeSuggestion(CryptoNetwork network) {
		if (fee_cache.containsKey(network)) {
			return fee_cache.get(network);
		} else if (network.isWeb3J()) { // Web3J 网络支持
			try {
				Web3j web3 = getWeb3j().orElseThrow(() -> new IOException("Web3j client not initialized"));
				BigInteger gasPriceWei = web3.ethGasPrice().send().getGasPrice(); // 获取当前 Gas 价格（Wei）

				// 计算低/标准/高手续费（单位：Wei，需转换为 SignumValue 兼容的最小单位）
				// 注意：以太坊最小单位是 Wei，与 Signum 的 NQT 类似，直接使用 gasPriceWei 构造 SignumValue
				SignumValue cheapFee = SignumValue.fromNQT(gasPriceWei.multiply(BigInteger.valueOf(80)).divide(BigInteger.valueOf(100))); // 80% 当前价
				SignumValue standardFee = SignumValue.fromNQT(gasPriceWei); // 当前价
				SignumValue priorityFee = SignumValue.fromNQT(gasPriceWei.multiply(BigInteger.valueOf(120)).divide(BigInteger.valueOf(100))); // 120% 当前价

				FeeSuggestion fee = new FeeSuggestion(cheapFee, standardFee, priorityFee); // 使用 SignumValue 构造
				fee_cache.put(network, fee);
				return fee;
			} catch (Exception e) {
				throw new RuntimeException("获取 Web3J 手续费建议失败", e);
			}
		}
		throw new UnsupportedOperationException("不支持的网络类型: " + network.getType());

	}

	public static final byte[] setRewardRecipient(CryptoNetwork network, String account, byte[] public_key, String recipient, BigDecimal fee) throws Exception {
		var server_url = network.getUrl();
		if (!server_url.endsWith("/")) {
			server_url += "/";
		}
		var client = new OkHttpClient.Builder().build();
		var request = new Request.Builder().url(server_url + "burst?requestType=setRewardRecipient")
				.post(RequestBody.create("deadline=1440&broadcast=false&feeNQT=" + toSignumValue(network, fee).toNQT() + "&publicKey=" + Hex.toHexString(public_key) + "&recipient=" + recipient,
						MediaType.parse("application/x-www-form-urlencoded")))
				.build();
		var response = client.newCall(request).execute();
		try {
			var jobj = new JSONObject(new JSONTokener(response.body().charStream()));
			if (jobj.optInt("errorCode", 0) != 0) {
				throw new IOException(jobj.optString("errorDescription"));
			}
			byte[] bArr = Hex.decode(jobj.getString("unsignedTransactionBytes"));
			return bArr;
		} finally {
			response.body().close();
			response.close();
		}
	}

	public static final Optional<String> getRewardRecipient(CryptoNetwork network, String account) throws Exception {
		if (account == null || account.isBlank()) {
			return Optional.empty();
		}
		if (network.isBurst()) {
			var server_url = network.getUrl();
			if (!server_url.endsWith("/")) {
				server_url += "/";
			}
			var client = new OkHttpClient.Builder().build();
			var request = new Request.Builder().url(server_url + "burst?requestType=getRewardRecipient&account=" + account).get().build();
			var response = client.newCall(request).execute();
			try {
				var jobj = new JSONObject(new JSONTokener(response.body().charStream()));
				if (jobj.optInt("errorCode", 0) == 5) {// Unknown account
					return Optional.empty();
				} else if (jobj.has("errorDescription")) {
					throw new Exception(jobj.getString("errorDescription"));
				} else if (jobj.has("rewardRecipient")) {
					return Optional.of(jobj.getString("rewardRecipient"));
				}
				return Optional.empty();
			} finally {
				response.body().close();
				response.close();
			}
		}
		throw new IllegalArgumentException();
	}

}
