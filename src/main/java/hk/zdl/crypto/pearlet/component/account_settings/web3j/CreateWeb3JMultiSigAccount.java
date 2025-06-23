package hk.zdl.crypto.pearlet.component.account_settings.web3j;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.math.BigInteger;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import org.web3j.abi.datatypes.Address;
import org.web3j.contracts.gnosissafe.generated.Safe;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.tx.gas.DynamicGasProvider;

import hk.zdl.crypto.pearlet.ds.CryptoNetwork;
import hk.zdl.crypto.pearlet.lock.CryptoAccount;
import hk.zdl.crypto.pearlet.misc.IndepandentWindows;
import hk.zdl.crypto.pearlet.util.CryptoUtil;
import hk.zdl.crypto.pearlet.util.Util;

public class CreateWeb3JMultiSigAccount {

	private static final ResourceBundle rsc_bdl = Util.getResourceBundle();
	/** Gnosis Safe 合约地址（主网默认地址：0x68b3465833fb72A70ecDF485Ee4263c06Fd50fda） **/
	private static final String safeAddress = "0x68b3465833fb72A70ecDF485Ee4263c06Fd50fda";

	public static final void create_new_multisig_account_dialog(Component c, CryptoNetwork nw) {
		var w = SwingUtilities.getWindowAncestor(c);
		if (!nw.isWeb3J()) {
			throw new IllegalArgumentException();
		}
		var text_area = new JTextArea(5, 20);
		text_area.setFont(new Font(Font.MONOSPACED, Font.PLAIN, text_area.getFont().getSize()));
		text_area.setLineWrap(false);
		var scrollPane = new JScrollPane(text_area);
		var requiredSigs = new JSpinner(new SpinnerNumberModel(2, 2, 10, 1));
		scrollPane.setPreferredSize(new Dimension(500, 150));

		Object[] message = { rsc_bdl.getString("MULTISIG.CREATION.ETH.INFO"), rsc_bdl.getString("MULTISIG.CREATION.ETH.OWNERS_LABEL"), scrollPane,
				rsc_bdl.getString("MULTISIG.CREATION.ETH.THRESHOLD_LABEL"), requiredSigs };

		var option = JOptionPane.showConfirmDialog(c, message, rsc_bdl.getString("MULTISIG.CREATION.ETH.TITLE"), JOptionPane.OK_CANCEL_OPTION);

		if (option == JOptionPane.OK_OPTION) {
			// 使用正则表达式分割换行输入（兼容前后空格）
			var owners = Stream.of(text_area.getText().split("\\s*\n\\s*")).map(String::trim).filter(s -> !s.isBlank()).collect(Collectors.toList());
			if (owners.isEmpty()) {
				JOptionPane.showMessageDialog(w, rsc_bdl.getString("ERROR.EMPTY_OWNERS"), rsc_bdl.getString("ERROR.INVALID_INPUT"), JOptionPane.ERROR_MESSAGE);
				return;
			}
			for (var owner : owners) {
				if (!owner.matches("^0x[a-fA-F0-9]{40}$")) {
					JOptionPane.showMessageDialog(w, rsc_bdl.getString("ERROR.INVALID_ADDRESS"), rsc_bdl.getString("GENERAL.ERROR"), JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
			var _threshold = new BigInteger(requiredSigs.getValue().toString());
			if (_threshold.intValue() > owners.size()) {
				JOptionPane.showMessageDialog(w, rsc_bdl.getString("ERROR.INVALID_THRESHOLD"), rsc_bdl.getString("ERROR.INVALID_INPUT"), JOptionPane.ERROR_MESSAGE);
				return;
			}
			byte[] private_key;
			try {
				private_key = owners.stream().map(s -> CryptoAccount.getAccount(nw, s.toLowerCase(Locale.ENGLISH))).flatMap(Optional::stream).findFirst()
						.orElseThrow(() -> new IllegalArgumentException("ERROR.NO_VALID_OWNER")).getPrivateKey();
			} catch (Exception e) {
				JOptionPane.showMessageDialog(w, e.getMessage(), rsc_bdl.getString("GENERAL.ERROR"), JOptionPane.ERROR_MESSAGE);
				return;
			}
			var dialog = new JDialog(w, rsc_bdl.getString("MULTISIG.CREATION.ETH.TITLE"), Dialog.ModalityType.APPLICATION_MODAL);
			dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
			IndepandentWindows.add(dialog);
			var panel = new JPanel();
			var bar = new JProgressBar();
			bar.setPreferredSize(new Dimension(500, 80));
			bar.setIndeterminate(true);
			panel.add(bar);
			dialog.add(panel);
			dialog.pack();
			dialog.setResizable(false);
			dialog.setLocationRelativeTo(w);
			var future = Util.submit(new MyWorker(private_key, owners, _threshold));
			dialog.setVisible(true);
			try {
				var receipt = future.get();
				if (receipt.isStatusOK()) {
					String tx_address = receipt.getTransactionHash();
					String contractAddress = receipt.getContractAddress(); // 直接从事务回执获取合约地址
					Object[] msg = { rsc_bdl.getString("MULTISIG.CREATION.SUCCESS"), contractAddress, tx_address };
					JOptionPane.showMessageDialog(w, msg, rsc_bdl.getString("SUCCESS.TITLE"), JOptionPane.INFORMATION_MESSAGE);
				} else {
					throw new TransactionException("ERROR.TX_FAILED");
				}
			} catch (Exception e) {
				dialog.setVisible(false);
				JOptionPane.showMessageDialog(w, e.getMessage(), rsc_bdl.getString("GENERAL.ERROR"), JOptionPane.ERROR_MESSAGE);
			} finally {
				dialog.setVisible(false);
			}
			
		}
	}

	private static class MyWorker implements Callable<TransactionReceipt> {

		private final byte[] private_key;
		private final List<String> owners;
		private final BigInteger threshold;

		private MyWorker(byte[] private_key, List<String> owners, BigInteger threshold) {
			this.private_key = private_key;
			this.owners = owners;
			this.threshold = threshold;
		}

		@Override
		public TransactionReceipt call() throws Exception {
			var credentials = Credentials.create(ECKeyPair.create(private_key));
			// 修改合约加载方式
			var contract = Safe.load(safeAddress, CryptoUtil.getWeb3j().get(), credentials, new DynamicGasProvider(CryptoUtil.getWeb3j().get()));
			// 修正setup方法参数类型
			var receipt = contract
					.setup(owners, threshold, Address.DEFAULT.toString(), new byte[] {}, Address.DEFAULT.toString(), Address.DEFAULT.toString(), BigInteger.ZERO, Address.DEFAULT.toString())
					.send();
			return receipt;
		}
		
	}
}
