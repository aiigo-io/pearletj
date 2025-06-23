package hk.zdl.crypto.pearlet.component.account_settings;

import java.awt.Component;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import hk.zdl.crypto.pearlet.component.account_settings.burst.CreateBurstAccount;
import hk.zdl.crypto.pearlet.component.account_settings.web3j.CreateWeb3JAccount;
import hk.zdl.crypto.pearlet.component.account_settings.web3j.CreateWeb3JMultiSigAccount;
import hk.zdl.crypto.pearlet.ds.CryptoNetwork;
import hk.zdl.crypto.pearlet.util.Util;

public class CreateAccount {

	private static final ResourceBundle rsc_bdl = Util.getResourceBundle();

	public static final void create_new_account_dialog(Component c, CryptoNetwork nw) {
		if (nw.isWeb3J()) {
			CreateWeb3JAccount.create_dialog(c, nw);
		} else if (nw.isBurst()) {
			CreateBurstAccount.create_dialog(c, nw);
		}
	}

	public static final void create_new_multisig_account_dialog(Component c, CryptoNetwork nw) {
		var w = SwingUtilities.getWindowAncestor(c);
		if (nw.isWeb3J()) {
			CreateWeb3JMultiSigAccount.create_new_multisig_account_dialog(c, nw);
		} else if (nw.isBurst()) {
			JOptionPane.showMessageDialog(w, rsc_bdl.getString("ERROR.MULTISIG_NOT_SUPPORTED"), rsc_bdl.getString("GENERAL.ERROR"), JOptionPane.ERROR_MESSAGE);
		}
	}
}
