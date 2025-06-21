package hk.zdl.crypto.pearlet.component.account_settings;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigInteger;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import org.greenrobot.eventbus.EventBus;
import org.java_websocket.util.Base64;
import org.jdesktop.swingx.combobox.EnumComboBoxModel;
import org.web3j.abi.datatypes.Address;
import org.web3j.contracts.gnosissafe.generated.Safe;
import org.web3j.crypto.Bip32ECKeyPair;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.MnemonicUtils;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.tx.gas.DynamicGasProvider;

import hk.zdl.crypto.pearlet.component.account_settings.burst.PKT;
import hk.zdl.crypto.pearlet.component.event.AccountListUpdateEvent;
import hk.zdl.crypto.pearlet.ds.CryptoNetwork;
import hk.zdl.crypto.pearlet.lock.CryptoAccount;
import hk.zdl.crypto.pearlet.misc.IndepandentWindows;
import hk.zdl.crypto.pearlet.ui.UIUtil;
import hk.zdl.crypto.pearlet.util.CryptoUtil;
import hk.zdl.crypto.pearlet.util.Util;

public class CreateAccount {

	private static final ResourceBundle rsc_bdl = Util.getResourceBundle();
	private static final Insets insets_5 = new Insets(5, 5, 5, 5);

	public static final void create_new_account_dialog(Component c, CryptoNetwork nw) {
		if (nw.isWeb3J()) {
			web3j(c, nw);
		} else if (nw.isBurst()) {
			burst(c, nw);
		}
	}

	@SuppressWarnings("unchecked")
	private static final void burst(Component c, CryptoNetwork nw) {
		var w = SwingUtilities.getWindowAncestor(c);
		var dialog = new JDialog(w, rsc_bdl.getString("SETTINGS.ACCOUNT.CREATE.TITLE"), Dialog.ModalityType.APPLICATION_MODAL);
		dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		IndepandentWindows.add(dialog);
		var panel = new JPanel(new GridBagLayout());
		panel.add(new JLabel(UIUtil.getStretchIcon("icon/" + "cloud-plus-fill.svg", 64, 64)), new GridBagConstraints(0, 0, 1, 4, 0, 0, 17, 0, insets_5, 0, 0));
		var label_1 = new JLabel(rsc_bdl.getString("SETTINGS.ACCOUNT.CREATE.NETWORK"));
		var network_combobox = new JComboBox<>(new String[] { nw.toString() });
		network_combobox.setEnabled(false);
		var label_2 = new JLabel(rsc_bdl.getString("SETTINGS.ACCOUNT.CREATE.TEXT_TYPE"));
		var combobox_1 = new JComboBox<>(new EnumComboBoxModel<>(PKT.class));
		panel.add(label_1, new GridBagConstraints(1, 0, 1, 1, 0, 0, 17, 0, insets_5, 0, 0));
		panel.add(network_combobox, new GridBagConstraints(2, 0, 1, 1, 0, 0, 17, 0, insets_5, 0, 0));
		panel.add(label_2, new GridBagConstraints(3, 0, 1, 1, 0, 0, 17, 0, insets_5, 0, 0));
		panel.add(combobox_1, new GridBagConstraints(4, 0, 1, 1, 0, 0, 17, 0, insets_5, 0, 0));
		var text_area = new JTextArea(5, 30);
		var scr_pane = new JScrollPane(text_area, ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		panel.add(scr_pane, new GridBagConstraints(1, 1, 4, 3, 0, 0, 17, 1, insets_5, 0, 0));
		text_area.setEditable(false);
		text_area.setFont(new Font(Font.MONOSPACED, Font.PLAIN, text_area.getFont().getSize()));

		var btn_1 = new JButton(rsc_bdl.getString("SETTINGS.ACCOUNT.CREATE.RANDOM"));
		var btn_2 = new JButton(rsc_bdl.getString("GENERAL.COPY"));
		var btn_3 = new JButton(UIManager.getString("OptionPane.okButtonText"));
		var panel_1 = new JPanel(new GridBagLayout());
		panel_1.add(btn_1, new GridBagConstraints(0, 0, 1, 1, 0, 0, 10, 0, insets_5, 0, 0));
		panel_1.add(btn_2, new GridBagConstraints(1, 0, 1, 1, 0, 0, 10, 0, insets_5, 0, 0));
		panel_1.add(btn_3, new GridBagConstraints(2, 0, 1, 1, 0, 0, 10, 0, insets_5, 0, 0));

		panel.add(panel_1, new GridBagConstraints(0, 5, 5, 1, 0, 0, 10, 1, new Insets(0, 0, 0, 0), 0, 0));

		btn_1.addActionListener(e -> {
			var sb = new StringBuilder();
			byte[] bArr = new byte[32];
			new Random().nextBytes(bArr);
			PKT type = (PKT) combobox_1.getSelectedItem();
			switch (type) {
			case Base64:
				text_area.setText(Base64.encodeBytes(bArr));
				break;
			case HEX:
				for (byte b : bArr) {
					sb.append(String.format("%02X", b));
					sb.append(' ');
				}
				text_area.setText(sb.toString().trim());
				break;
			case Phrase:
				text_area.setText(get_mnemoic());
				break;
			}
		});
		btn_2.addActionListener(e -> {
			var s = new StringSelection(text_area.getText().trim());
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(s, s);
		});
		btn_3.addActionListener(e -> Util.submit(() -> {
			var type = (PKT) combobox_1.getSelectedItem();
			var text = text_area.getText().trim();
			try {
				if (WalletUtil.insert_burst_account(nw, type, text)) {
					UIUtil.displayMessage(rsc_bdl.getString("SETTINGS.ACCOUNT.CREATE.TITLE"), rsc_bdl.getString("GENERAL.DONE"));
					EventBus.getDefault().post(new AccountListUpdateEvent());
				} else {
					JOptionPane.showMessageDialog(w, rsc_bdl.getString("GENERAL.DUP"), rsc_bdl.getString("GENERAL.ERROR"), JOptionPane.ERROR_MESSAGE);
				}
			} catch (Exception x) {
				JOptionPane.showMessageDialog(w, x.getMessage(), x.getClass().getSimpleName(), JOptionPane.ERROR_MESSAGE);
				return;
			}
		}));
		combobox_1.addActionListener((e) -> btn_1.doClick());

		dialog.add(panel);
		dialog.pack();
		dialog.setResizable(false);
		dialog.setLocationRelativeTo(w);
		dialog.addWindowListener(new WindowAdapter() {

			@Override
			public void windowOpened(WindowEvent e) {
				btn_1.doClick();
			}

		});

		dialog.getRootPane().registerKeyboardAction(e -> dialog.dispose(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
		dialog.getRootPane().registerKeyboardAction(e -> btn_3.doClick(), KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
		dialog.setVisible(true);
	}

	private static synchronized String get_mnemoic() {
		var bArr = new byte[16];
		new Random().nextBytes(bArr);
		return MnemonicUtils.generateMnemonic(bArr);
	}

	private static final void web3j(Component c, CryptoNetwork nw) {
		var w = SwingUtilities.getWindowAncestor(c);
		var icon = UIUtil.getStretchIcon("icon/" + "cloud-plus-fill.svg", 64, 64);
		var panel = new JPanel(new GridBagLayout());

		var mm_label = new JLabel(rsc_bdl.getString("SETTINGS.ACCOUNT.CREATE.INPUT_MNC"));
		var text_area = new JTextArea(5, 30);
		var scr_pane = new JScrollPane(text_area, ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		panel.add(mm_label, new GridBagConstraints(0, 1, 1, 1, 0, 0, 17, 1, insets_5, 0, 0));
		panel.add(scr_pane, new GridBagConstraints(0, 2, 2, 1, 0, 0, 17, 1, insets_5, 0, 0));
		Util.submit(() -> text_area.setText(get_mnemoic()));

		var i = JOptionPane.showConfirmDialog(w, panel, rsc_bdl.getString("SETTINGS.ACCOUNT.CREATE.TITLE"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, icon);
		if (i == JOptionPane.CANCEL_OPTION) {
			return;
		} else if (i == JOptionPane.OK_OPTION) {
			var mnemonic = text_area.getText().trim();
			if (mnemonic.isBlank()) {
				JOptionPane.showMessageDialog(w, rsc_bdl.getString("SETTINGS.ACCOUNT.CREATE.REQUIRE_MNC"), rsc_bdl.getString("GENERAL.ERROR"), JOptionPane.ERROR_MESSAGE);
				return;
			}
			try {
				// 生成BIP-39种子（与 load_from_mnemonic 保持一致）
				byte[] seed = MnemonicUtils.generateSeed(mnemonic, "");

				// 使用BIP-32路径派生密钥对（与 load_from_mnemonic 路径一致）
				int[] path = { 44 | Bip32ECKeyPair.HARDENED_BIT, // 44' (purpose)
						60 | Bip32ECKeyPair.HARDENED_BIT, // 60' (coin type)
						0 | Bip32ECKeyPair.HARDENED_BIT, // 0' (account)
						0, // 0 (change)
						0 // 0 (address index)
				};
				var masterKeyPair = Bip32ECKeyPair.generateKeyPair(seed);
				var derivedKeyPair = Bip32ECKeyPair.deriveKeyPair(masterKeyPair, path);

				// 直接使用派生的密钥对创建凭证
				var cred = Credentials.create(derivedKeyPair);

				if (WalletUtil.insert_web3j_account(nw, cred.getEcKeyPair())) {
					UIUtil.displayMessage(rsc_bdl.getString("SETTINGS.ACCOUNT.CREATE.TITLE"), rsc_bdl.getString("GENERAL.DONE"));
					EventBus.getDefault().post(new AccountListUpdateEvent());
				} else {
					JOptionPane.showMessageDialog(w, rsc_bdl.getString("GENERAL.DUP"), rsc_bdl.getString("GENERAL.ERROR"), JOptionPane.ERROR_MESSAGE);
				}
			} catch (Exception e) {
				JOptionPane.showMessageDialog(w, e.getMessage(), e.getClass().getSimpleName(), JOptionPane.ERROR_MESSAGE);
				return;
			}
		}
	}

	public static final void create_new_multisig_account_dialog(Component c, CryptoNetwork nw) {
		var w = SwingUtilities.getWindowAncestor(c);
		if (nw.isWeb3J()) {
			var ownersArea = new JTextArea(5, 20);
			ownersArea.setLineWrap(true);
			var scrollPane = new JScrollPane(ownersArea);
			var requiredSigs = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
			scrollPane.setPreferredSize(new Dimension(500, 150));

			Object[] message = { rsc_bdl.getString("MULTISIG.CREATION.ETH.INFO"), rsc_bdl.getString("MULTISIG.CREATION.ETH.OWNERS_LABEL"), scrollPane,
					rsc_bdl.getString("MULTISIG.CREATION.ETH.THRESHOLD_LABEL"), requiredSigs };

			var option = JOptionPane.showConfirmDialog(c, message, rsc_bdl.getString("MULTISIG.CREATION.ETH.TITLE"), JOptionPane.OK_CANCEL_OPTION);

			if (option == JOptionPane.OK_OPTION) {
				// 使用正则表达式分割换行输入（兼容前后空格）
				var owners = Stream.of(ownersArea.getText().split("\\s*\n\\s*")).map(String::trim).filter(s -> !s.isBlank()).collect(Collectors.toList());
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
					private_key = owners.stream().map(s -> CryptoAccount.getAccount(nw, s)).flatMap(Optional::stream).findFirst()
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
				dialog.setVisible(true);
				Util.submit(() -> {
					try {
						var credentials = Credentials.create(ECKeyPair.create(private_key));
						// Gnosis Safe 合约地址（主网默认地址：0x68b3465833fb72A70ecDF485Ee4263c06Fd50fda）
						var safeAddress = "0x68b3465833fb72A70ecDF485Ee4263c06Fd50fda";
						// 修改合约加载方式
						var contract = Safe.load(safeAddress, CryptoUtil.getWeb3j().get(), credentials, new DynamicGasProvider(CryptoUtil.getWeb3j().get()));
						// 修正setup方法参数类型
						var receipt = contract.setup(owners, _threshold, Address.DEFAULT.toString(), new byte[] {}, Address.DEFAULT.toString(), Address.DEFAULT.toString(), BigInteger.ZERO,
								Address.DEFAULT.toString()).send();
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
					return null;
				});
			} else if (nw.isBurst()) {
				JOptionPane.showMessageDialog(w, rsc_bdl.getString("ERROR.MULTISIG_NOT_SUPPORTED"), rsc_bdl.getString("GENERAL.ERROR"), JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
