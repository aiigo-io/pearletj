package hk.zdl.crypto.pearlet.tx;

import java.math.BigDecimal;
import java.util.Optional;

import hk.zdl.crypto.pearlet.ds.CryptoNetwork;
import hk.zdl.crypto.pearlet.lock.CryptoAccount;
import hk.zdl.crypto.pearlet.util.CryptoUtil;

public class BurstSendTx extends SendTx {

    // 注意：父类已包含 network/from/to/amount/fee/asset_id 等属性，无需重复声明
    private final byte[] binMessage;
    private final String strMessage;

    private BurstSendTx(Builder builder) {
        super(builder.network, builder.from, builder.to, builder.amount, builder.fee, builder.assetId);
        this.isEncrypted = builder.isEncrypted;
        this.binMessage = builder.binMessage;
        this.strMessage = builder.strMessage;
        // 同步消息字段到父类（确保父类 setMessage 方法修改的字段与子类一致）
        this.bin_message = binMessage;
        this.str_message = strMessage;
    }

    @Override
    public Boolean call() throws Exception {
        Optional<CryptoAccount> accountOpt = CryptoAccount.getAccount(network, from);
        if (!accountOpt.isPresent()) {
            return false;
        }

        // 补充消息长度验证（原 SendTx 中 Burst 分支的逻辑）
        if (strMessage != null && !strMessage.isBlank() && strMessage.getBytes().length > 1000) {
            return false;
        }
        if (binMessage != null && binMessage.length > 1000) {
            return false;
        }

        byte[] privateKey = accountOpt.get().getPrivateKey();
        byte[] publicKey = accountOpt.get().getPublicKey();
        byte[] txBytes = generateBurstTransaction(publicKey);
        byte[] signedTx = CryptoUtil.signTransaction(network, privateKey, txBytes);
        return CryptoUtil.broadcastTransaction(network, signedTx) != null;
    }

    private byte[] generateBurstTransaction(byte[] publicKey) throws Exception {
        if (asset_id == null) { // 父类中 asset_id 替代原 assetId
            return handleBurstNativeTransaction(publicKey);
        } else {
            return handleBurstAssetTransaction(publicKey);
        }
    }

    // 辅助方法保持不变（调整 assetId 为 asset_id 以匹配父类）
    private byte[] handleBurstNativeTransaction(byte[] publicKey) throws Exception {
        if (strMessage != null && !strMessage.isBlank()) {
            return isEncrypted ? CryptoUtil.generateTransactionWithEncryptedMessage(network, to, publicKey, amount, fee, strMessage.getBytes(), true)
                    : CryptoUtil.generateTransactionWithMessage(network, to, publicKey, amount, fee, strMessage);
        } else if (binMessage != null) {
            return isEncrypted ? CryptoUtil.generateTransactionWithEncryptedMessage(network, to, publicKey, amount, fee, binMessage, false)
                    : CryptoUtil.generateTransactionWithMessage(network, to, publicKey, amount, fee, binMessage);
        } else {
            return CryptoUtil.generateTransaction(network, to, publicKey, amount, fee);
        }
    }

    private byte[] handleBurstAssetTransaction(byte[] publicKey) throws Exception {
        if (strMessage != null && !strMessage.isBlank()) {
            return isEncrypted ? CryptoUtil.generateTransferAssetTransactionWithEncryptedMessage(network, publicKey, to, asset_id, amount, fee, strMessage.getBytes(), true)
                    : CryptoUtil.generateTransferAssetTransactionWithMessage(network, publicKey, to, asset_id, amount, fee, strMessage);
        } else if (binMessage != null) {
            return isEncrypted ? CryptoUtil.generateTransferAssetTransactionWithEncryptedMessage(network, publicKey, to, asset_id, amount, fee, binMessage, false)
                    : CryptoUtil.generateTransferAssetTransactionWithMessage(network, publicKey, to, asset_id, amount, fee, binMessage);
        } else {
            return CryptoUtil.generateTransferAssetTransaction(network, publicKey, to, asset_id, amount, fee);
        }
    }

    public static class Builder {
        private CryptoNetwork network;
        private String from;
        private String to;
        private BigDecimal amount;
        private BigDecimal fee;
        private String assetId;
        private boolean isEncrypted;
        private byte[] binMessage;
        private String strMessage;

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

        public Builder encrypted(boolean encrypted) {
            isEncrypted = encrypted;
            return this;
        }

        public Builder binMessage(byte[] binMessage) {
            this.binMessage = binMessage;
            return this;
        }

        public Builder strMessage(String strMessage) {
            this.strMessage = strMessage;
            return this;
        }

        public BurstSendTx build() {
            return new BurstSendTx(this);
        }
    }
}