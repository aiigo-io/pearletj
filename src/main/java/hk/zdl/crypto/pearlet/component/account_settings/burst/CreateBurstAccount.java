package hk.zdl.crypto.pearlet.component.account_settings.burst;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Random;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import org.greenrobot.eventbus.EventBus;
import org.java_websocket.util.Base64;
import org.jdesktop.swingx.combobox.EnumComboBoxModel;
import org.web3j.crypto.MnemonicUtils;

import hk.zdl.crypto.pearlet.component.account_settings.WalletUtil;
import hk.zdl.crypto.pearlet.component.event.AccountListUpdateEvent;
import hk.zdl.crypto.pearlet.ds.CryptoNetwork;
import hk.zdl.crypto.pearlet.misc.IndepandentWindows;
import hk.zdl.crypto.pearlet.ui.UIUtil;
import hk.zdl.crypto.pearlet.util.Util;

public class CreateBurstAccount {

	private static final ResourceBundle rsc_bdl = Util.getResourceBundle();
	private static final Insets insets_5 = new Insets(5, 5, 5, 5);

	@SuppressWarnings("unchecked")
	public static final void create_dialog(Component c, CryptoNetwork nw) {
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
		text_area.setLineWrap(false);
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

}
