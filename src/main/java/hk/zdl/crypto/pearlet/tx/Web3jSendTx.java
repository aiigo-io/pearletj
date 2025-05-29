package hk.zdl.crypto.pearlet.tx;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.Callable;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.contracts.eip20.generated.ERC20;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Numeric;

import hk.zdl.crypto.pearlet.ds.CryptoNetwork;
import hk.zdl.crypto.pearlet.lock.CryptoAccount;
import hk.zdl.crypto.pearlet.util.CryptoUtil;

public class Web3jSendTx extends SendTx { // 继承抽象父类 SendTx

    // 移除重复声明（父类已包含 network/from/to/amount/fee/asset_id 等属性）

    private Web3jSendTx(Builder builder) {
        super(builder.network, builder.from, builder.to, builder.amount, builder.fee, builder.assetId); // 调用父类构造器
    }

    @Override
    public Boolean call() throws Exception {
        Optional<CryptoAccount> accountOpt = CryptoAccount.getAccount(network, from); // 直接使用父类 protected 属性
        if (!accountOpt.isPresent())
            return false;

        Credentials credentials = Credentials.create(ECKeyPair.create(accountOpt.get().getPrivateKey()));
        Optional<Web3j> web3Opt = CryptoUtil.getWeb3j();
        if (!web3Opt.isPresent())
            return false;

        Web3j web3 = web3Opt.get();
        long chainId = web3.ethChainId().send().getChainId().longValue();
        BigInteger nonce = web3.ethGetTransactionCount(credentials.getAddress(), DefaultBlockParameterName.PENDING).send().getTransactionCount();
        BigInteger gasPrice = web3.ethGasPrice().send().getGasPrice();

        if (isNativeEthTransaction()) {
            sendEthTransaction(web3, credentials, chainId, nonce, gasPrice);
        } else {
            sendErc20Transaction(web3, credentials, chainId, nonce, gasPrice);
        }
        return true;
    }

    private boolean isNativeEthTransaction() {
        // 使用父类 asset_id 替代本地 assetId
        return asset_id == null || Arrays.asList("0x0000000000000000000000000000000000000000", "0xeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee").contains(asset_id);
    }

    private void sendEthTransaction(Web3j web3, Credentials credentials, long chainId, BigInteger nonce, BigInteger gasPrice) throws Exception {
        // 使用父类 to/amount 属性
        RawTransaction tx = RawTransaction.createEtherTransaction(nonce, gasPrice, BigInteger.valueOf(21000), to, amount.toBigInteger());
        byte[] signedTx = TransactionEncoder.signMessage(tx, chainId, credentials);
        web3.ethSendRawTransaction(Numeric.toHexString(signedTx)).send();
    }

    private void sendErc20Transaction(Web3j web3, Credentials credentials, long chainId, BigInteger nonce, BigInteger gasPrice) throws Exception {
        // 使用父类 asset_id/amount 属性
        ERC20 erc20 = ERC20.load(asset_id, web3, credentials, new DefaultGasProvider());
        int decimals = erc20.decimals().send().intValue();
        BigInteger amountWei = amount.movePointRight(decimals).toBigIntegerExact();

        Function transferFunc = new Function("transfer", Arrays.asList(new Address(to), new Uint256(amountWei)), Collections.singletonList(new TypeReference<Bool>() {
        }));
        String data = FunctionEncoder.encode(transferFunc);

        RawTransaction tx = RawTransaction.createTransaction(nonce, gasPrice, BigInteger.valueOf(100000), asset_id, data);
        byte[] signedTx = TransactionEncoder.signMessage(tx, chainId, credentials);
        web3.ethSendRawTransaction(Numeric.toHexString(signedTx)).send();
    }

    public static class Builder {
        private CryptoNetwork network;
        private String from;
        private String to;
        private BigDecimal amount;
        private BigDecimal fee;
        private String assetId;

        public Builder network(CryptoNetwork network) {
            this.network = network;
            return this;
        }

        public Builder from(String from) {
            this.from = from;
            return this;
        }

        public Builder to(String to) {
            this.to = to;
            return this;
        }

        public Builder amount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public Builder fee(BigDecimal fee) {
            this.fee = fee;
            return this;
        }

        public Builder assetId(String assetId) {
            this.assetId = assetId;
            return this;
        }

        public Web3jSendTx build() {
            return new Web3jSendTx(this);
        }
    }
}