package hk.zdl.crypto.pearlet.component.account_settings.web3j;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Random;
import java.util.ResourceBundle;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
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

public class CreateWeb3JAccount {

	private static final ResourceBundle rsc_bdl = Util.getResourceBundle();
	private static final Insets insets_5 = new Insets(5, 5, 5, 5);
	
	private static synchronized String get_mnemoic() {
		var bArr = new byte[16];
		new Random().nextBytes(bArr);
		return MnemonicUtils.generateMnemonic(bArr);
	}

	public static final void create_dialog(Component c, CryptoNetwork nw) {
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

}
