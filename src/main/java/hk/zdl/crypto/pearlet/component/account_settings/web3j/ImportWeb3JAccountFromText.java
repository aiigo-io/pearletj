package hk.zdl.crypto.pearlet.component.account_settings.web3j;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.MessageFormat;
import java.util.ResourceBundle;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import org.greenrobot.eventbus.EventBus;
import org.web3j.crypto.Bip32ECKeyPair;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.MnemonicUtils;

import hk.zdl.crypto.pearlet.component.account_settings.WalletUtil;
import hk.zdl.crypto.pearlet.component.event.AccountListUpdateEvent;
import hk.zdl.crypto.pearlet.ds.CryptoNetwork;
import hk.zdl.crypto.pearlet.ui.UIUtil;
import hk.zdl.crypto.pearlet.util.Util;

public class ImportWeb3JAccountFromText {

	private static final ResourceBundle rsc_bdl = Util.getResourceBundle();
	private static final Insets insets_5 = new Insets(5, 5, 5, 5);

	public static final void import_from_private_key(Component c, CryptoNetwork nw) {
		var w = SwingUtilities.getWindowAncestor(c);
		Icon icon = UIUtil.getStretchIcon("icon/" + "wallet_2.svg", 64, 64);
		var panel = new JPanel(new GridBagLayout());

		var mm_label = new JLabel(rsc_bdl.getString("SETTINGS.ACCOUNT.IMPORT.ENTER_PRIVATE_KEY_HEX"));
		var tx_field = new JTextArea(5, 20);
		var sc_panee = new JScrollPane(tx_field);
		panel.add(mm_label, new GridBagConstraints(0, 0, 1, 1, 0, 0, 17, 1, insets_5, 0, 0));
		panel.add(sc_panee, new GridBagConstraints(0, 1, 2, 1, 0, 0, 17, 1, insets_5, 0, 0));

		var pane = new JOptionPane(panel, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION, icon);
		var dlg = pane.createDialog(w, rsc_bdl.getString("SETTINGS.ACCOUNT.IMPORT.TITLE"));
		dlg.addWindowFocusListener(new WindowAdapter() {
			@Override
			public void windowGainedFocus(WindowEvent e) {
				tx_field.grabFocus();
			}
		});
		dlg.setVisible(true);
		if ((int) pane.getValue() != JOptionPane.OK_OPTION) {
			return;
		}
		var private_key_str = tx_field.getText().trim();

		if (private_key_str.isBlank()) {
			JOptionPane.showMessageDialog(w, rsc_bdl.getString("SETTINGS.ACCOUNT.IMPORT.PRIVATE_KEY_CANNOT_EMPTY"), rsc_bdl.getString("GENERAL.ERROR"), JOptionPane.ERROR_MESSAGE);
			return;
		}

		Credentials cred = null;

		try {
			cred = Credentials.create(private_key_str);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(w, e.getMessage(), e.getClass().getSimpleName(), JOptionPane.ERROR_MESSAGE);
			return;
		}

		try {
			if (WalletUtil.insert_web3j_account(nw, cred.getEcKeyPair())) {
				UIUtil.displayMessage(rsc_bdl.getString("SETTINGS.ACCOUNT.IMPORT.TITLE"), rsc_bdl.getString("GENERAL.DONE"));
				EventBus.getDefault().post(new AccountListUpdateEvent());
			} else {
				JOptionPane.showMessageDialog(w, rsc_bdl.getString("GENERAL.DUP"), rsc_bdl.getString("GENERAL.ERROR"), JOptionPane.ERROR_MESSAGE);
			}
		} catch (Exception x) {
			JOptionPane.showMessageDialog(w, x.getMessage(), x.getClass().getSimpleName(), JOptionPane.ERROR_MESSAGE);
		}

	}

	public static final void load_from_mnemonic(Component c, CryptoNetwork nw) {
		var w = SwingUtilities.getWindowAncestor(c);
		Icon icon = UIUtil.getStretchIcon("icon/" + "wallet_2.svg", 64, 64);
		var panel = new JPanel(new GridBagLayout());

		var mm_label = new JLabel(rsc_bdl.getString("SETTINGS.ACCOUNT.IMPORT.ENTER_MNC"));
		var tx_field = new JTextArea(5, 20);
		var sc_panee = new JScrollPane(tx_field);
		panel.add(mm_label, new GridBagConstraints(0, 0, 1, 1, 0, 0, 17, 1, insets_5, 0, 0));
		panel.add(sc_panee, new GridBagConstraints(0, 1, 2, 1, 0, 0, 17, 1, insets_5, 0, 0));

		int i = JOptionPane.showConfirmDialog(w, panel, rsc_bdl.getString("SETTINGS.ACCOUNT.IMPORT.TITLE"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, icon);
		if (i != JOptionPane.OK_OPTION) {
			return;
		}
		var mnemonic = tx_field.getText().trim();

		if (mnemonic.isBlank()) {
			JOptionPane.showMessageDialog(w, rsc_bdl.getString("SETTINGS.ACCOUNT.IMPORT.MNC_CANNOT_EMPTY"), rsc_bdl.getString("GENERAL.ERROR"), JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		try {
			// 1. 生成BIP-39种子（保持不变）
			byte[] seed = MnemonicUtils.generateSeed(mnemonic, "");

			// 2. 使用Bip32ECKeyPair派生密钥
			int[] path = { 44 | Bip32ECKeyPair.HARDENED_BIT, // 44' (purpose)
					60 | Bip32ECKeyPair.HARDENED_BIT, // 60' (coin type)
					0 | Bip32ECKeyPair.HARDENED_BIT, // 0' (account)
					0, // 0 (change)
					0 // 0 (address index)
			};
			Bip32ECKeyPair masterKeyPair = Bip32ECKeyPair.generateKeyPair(seed);
			Bip32ECKeyPair derivedKeyPair = Bip32ECKeyPair.deriveKeyPair(masterKeyPair, path);

			// 3. 转换为凭证（直接使用Bip32ECKeyPair创建）
			Credentials cred = Credentials.create(derivedKeyPair);

			// 后续保存逻辑不变
			if (WalletUtil.insert_web3j_account(nw, cred.getEcKeyPair())) {
				UIUtil.displayMessage(rsc_bdl.getString("SETTINGS.ACCOUNT.IMPORT.TITLE"), rsc_bdl.getString("GENERAL.DONE"));
				EventBus.getDefault().post(new AccountListUpdateEvent());
			} else {
				JOptionPane.showMessageDialog(w, rsc_bdl.getString("GENERAL.DUP"), rsc_bdl.getString("GENERAL.ERROR"), JOptionPane.ERROR_MESSAGE);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(w, MessageFormat.format(rsc_bdl.getString("SETTINGS.ACCOUNT.IMPORT.ERR_LOAD"), e.getMessage()), e.getClass().getSimpleName(), JOptionPane.ERROR_MESSAGE);
		}
	}
}
