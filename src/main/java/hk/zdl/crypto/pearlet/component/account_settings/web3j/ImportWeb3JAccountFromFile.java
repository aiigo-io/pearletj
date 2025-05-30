package hk.zdl.crypto.pearlet.component.account_settings.web3j;

import java.awt.Component;
import java.io.File;
import java.util.ResourceBundle;

import javax.swing.Icon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;

import org.greenrobot.eventbus.EventBus;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;

import hk.zdl.crypto.pearlet.component.account_settings.WalletUtil;
import hk.zdl.crypto.pearlet.component.event.AccountListUpdateEvent;
import hk.zdl.crypto.pearlet.ds.CryptoNetwork;
import hk.zdl.crypto.pearlet.ui.UIUtil;
import hk.zdl.crypto.pearlet.util.Util;

public class ImportWeb3JAccountFromFile {
	private static final ResourceBundle rsc_bdl = Util.getResourceBundle();

	public static final void create_import_account_dialog(Component c, CryptoNetwork nw) {
		var w = SwingUtilities.getWindowAncestor(c);
		Icon icon = UIUtil.getStretchIcon("icon/" + "wallet_2.svg", 64, 64);
		var file_dialog = new JFileChooser();
		file_dialog.setDialogType(JFileChooser.OPEN_DIALOG);
		file_dialog.setMultiSelectionEnabled(false);
		file_dialog.setDragEnabled(false);
		file_dialog.setFileSelectionMode(JFileChooser.FILES_ONLY);
		file_dialog.setFileFilter(new FileFilter() {

			@Override
			public String getDescription() {
				return rsc_bdl.getString("GENERAL.JSON_FILES");
			}

			@Override
			public boolean accept(File f) {
				return f.isDirectory() || f.getName().toLowerCase().endsWith(".json");
			}
		});
		int i = file_dialog.showOpenDialog(w);
		if (i != JFileChooser.APPROVE_OPTION) {
			return;
		}
		File file = file_dialog.getSelectedFile();
		Credentials cred = null;
		try {
			cred = WalletUtils.loadCredentials("", file);
		} catch (Exception e) {
		}
		var pw_field = new JPasswordField(20);
		int j = JOptionPane.showConfirmDialog(w, pw_field, rsc_bdl.getString("SETTINGS.ACCOUNT.CREATE.INPUT_PW"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, icon);
		if (j != JOptionPane.OK_OPTION) {
			return;
		}
		try {
			cred = WalletUtils.loadCredentials(new String(pw_field.getPassword()), file);
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
}
