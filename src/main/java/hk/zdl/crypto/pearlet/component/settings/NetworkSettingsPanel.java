package hk.zdl.crypto.pearlet.component.settings;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import com.jthemedetecor.OsThemeDetector;

import hk.zdl.crypto.pearlet.component.event.AccountListUpdateEvent;
import hk.zdl.crypto.pearlet.component.event.NetworkChangeEvent;
import hk.zdl.crypto.pearlet.component.settings.wizard.ChooseNetworkType;
import hk.zdl.crypto.pearlet.component.settings.wizard.ConfirmNetwork;
import hk.zdl.crypto.pearlet.component.settings.wizard.EnterNetworkDetail;
import hk.zdl.crypto.pearlet.component.settings.wizard.JDialogWizard;
import hk.zdl.crypto.pearlet.ds.CryptoNetwork;
import hk.zdl.crypto.pearlet.misc.VerticalFlowLayout;
import hk.zdl.crypto.pearlet.persistence.MyDb;
import hk.zdl.crypto.pearlet.ui.UIUtil;
import hk.zdl.crypto.pearlet.util.CryptoUtil;
import hk.zdl.crypto.pearlet.util.Util;

public class NetworkSettingsPanel extends JPanel {

	private static final long serialVersionUID = 6897317905255689373L;
	private static final ResourceBundle rsc_bdl = Util.getResourceBundle();
	private static final Insets insets_5 = new Insets(2, 3, 3, 3);
	private static final JPanel center_panel = new JPanel(new GridLayout(0, 1));
	private static final int ping_timeout = 5000;

	public NetworkSettingsPanel() {
		super(new BorderLayout());
		EventBus.getDefault().register(this);

		var btn_panel = new JPanel(new VerticalFlowLayout());
		var add_btn = new JButton(UIUtil.getStretchIcon("icon/heavy-plus-sign-svgrepo-com.svg", 32, 32));
		btn_panel.add(add_btn);

		add_btn.addActionListener(e -> {
			var startPage = new ChooseNetworkType();
			var detailPage = new EnterNetworkDetail();
			startPage.setNextPage(detailPage);
			var confPage = new ConfirmNetwork();
			detailPage.setNextPage(confPage);
			var finish = JDialogWizard.showWizard(rsc_bdl.getString("SETTINGS.NETWORK.PANEL.ADD"), startPage, this);
			if (finish) {
				var new_network = new CryptoNetwork();
				new_network.setName(confPage.getNetworkName());
				new_network.setUrl(confPage.getNetworkAddress());
				switch (confPage.getType()) {
				case "Ethereum":
					new_network.setType(CryptoNetwork.Type.WEB3J);
					break;
				case "Burst Variant":
					new_network.setType(CryptoNetwork.Type.BURST);
					break;
				}
				if (MyDb.insert_network(new_network)) {
					EventBus.getDefault().post(new NetworkChangeEvent());
				}
			}
		});

		var panel_1 = new JPanel(new FlowLayout(1, 0, 0));
		panel_1.add(btn_panel);
		add(panel_1, BorderLayout.EAST);

		var p = new JPanel();
		p.add(center_panel);
		add(new JScrollPane(p, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);
		EventBus.getDefault().post(new NetworkChangeEvent());
	}

	@Subscribe(threadMode = ThreadMode.ASYNC)
	public void onMessage(NetworkChangeEvent e) {
		SwingUtilities.invokeLater(() -> {
			center_panel.removeAll();
			MyDb.get_networks().stream().map(this::init_network_UI_components).forEach(center_panel::add);
			SwingUtilities.updateComponentTreeUI(this);
		});
	}

	@SuppressWarnings("deprecation")
	private final Component init_network_UI_components(CryptoNetwork o) {
		var panel = new JPanel(new BorderLayout());
		panel.setPreferredSize(new Dimension(700, 130));
		var icon = UIUtil.getStretchIcon("icon/" + Util.get_icon_file_name(o, false), 64, 64);
		var icon_dark = UIUtil.getStretchIcon("icon/" + Util.get_icon_file_name(o, true), 64, 64);
		var icon_label = new JLabel();
		OsThemeDetector.getDetector().registerListener((isDark) -> {
			icon_label.setIcon(isDark ? icon_dark : icon);
		});
		icon_label.setIcon(OsThemeDetector.getDetector().isDark() ? icon_dark : icon);
		panel.add(icon_label, BorderLayout.WEST);
		var my_panel = new JPanel(new GridBagLayout());
		panel.add(my_panel, BorderLayout.CENTER);
		my_panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), o.getName(), TitledBorder.LEFT, TitledBorder.TOP,
				new Font("Arial Black", Font.PLAIN, (int) (getFont().getSize() * 1.5))));
		var label_0 = new JLabel(rsc_bdl.getString("SETTINGS.NETWORK.PANEL.TYPE"));
		var label_1 = new JLabel(rsc_bdl.getString("SETTINGS.NETWORK.PANEL.URL"));
		var label_2 = new JLabel(rsc_bdl.getString("SETTINGS.NETWORK.PANEL.PING"));

		var label_3 = new JLabel(o.getType().name());
		var label_4 = new JLabel(o.getUrl());
		var prog = new JProgressBar();
		prog.setString("");
		prog.setStringPainted(true);

		var btn_0 = new JButton(rsc_bdl.getString("SETTINGS.NETWORK.PANEL.DEL"));
		var btn_1 = new JButton(rsc_bdl.getString("SETTINGS.NETWORK.PANEL.MOD"));
		var btn_2 = new JButton("Ping");
		var btn_3 = new JButton(rsc_bdl.getString("SETTINGS.NETWORK.PANEL.Credential"));
		if (o.getType() == CryptoNetwork.Type.WEB3J) {
			my_panel.add(btn_3, new GridBagConstraints(2, 0, 1, 1, 1, 0, GridBagConstraints.EAST, GridBagConstraints.NONE, insets_5, 0, 0));
			btn_3.addActionListener(e -> createWeb3jAuthDialog(this));
		}
		my_panel.add(label_0, new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, insets_5, 0, 0));
		my_panel.add(label_1, new GridBagConstraints(0, 1, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, insets_5, 0, 0));
		my_panel.add(label_2, new GridBagConstraints(0, 2, 1, 1, 0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, insets_5, 0, 0));

		my_panel.add(label_3, new GridBagConstraints(1, 0, 1, 1, 1, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, insets_5, 0, 0));
		my_panel.add(label_4, new GridBagConstraints(1, 1, 2, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, insets_5, 0, 0));
		my_panel.add(prog, new GridBagConstraints(1, 2, 2, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, insets_5, 0, 0));

		my_panel.add(btn_0, new GridBagConstraints(3, 0, 1, 1, 0, 0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, insets_5, 0, 0));
		my_panel.add(btn_1, new GridBagConstraints(3, 1, 1, 1, 0, 0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, insets_5, 0, 0));
		my_panel.add(btn_2, new GridBagConstraints(3, 2, 1, 1, 0, 0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, insets_5, 0, 0));

		btn_0.addActionListener(e -> {
			if (JOptionPane.showConfirmDialog(getRootPane(), rsc_bdl.getString("SETTINGS.NETWORK.PANEL.CONFIRN_DELETE"), null, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
				MyDb.delete_network(o.getId());
				EventBus.getDefault().post(new NetworkChangeEvent());
				EventBus.getDefault().post(new AccountListUpdateEvent());
			}
		});
		btn_2.addActionListener(e -> {
			btn_2.setEnabled(false);
			prog.setString("");
			prog.setIndeterminate(true);
			Util.submit(() -> {
				var f = Util.submit(() -> {
					var t = System.currentTimeMillis();
					new URL(o.getUrl()).openStream().close();
					t = System.currentTimeMillis() - t;
					prog.setIndeterminate(false);
					prog.setString(MessageFormat.format(rsc_bdl.getString("SETTINGS.NETWORK.PANEL.PING_TIME"), t));
					btn_2.setEnabled(true);
					return null;
				});
				try {
					f.get(ping_timeout, TimeUnit.MILLISECONDS);
				} catch (TimeoutException x) {
					prog.setString(MessageFormat.format(rsc_bdl.getString("SETTINGS.NETWORK.PANEL.PING_TIME_OUT"), ping_timeout));
				} catch (Throwable x) {
					while (x.getCause() != null) {
						x = x.getCause();
					}
					prog.setString(x.getMessage());
				} finally {
					prog.setIndeterminate(false);
					btn_2.setEnabled(true);
				}
			});
		});
		btn_1.addActionListener(e -> {
			var str = JOptionPane.showInputDialog(getRootPane(), rsc_bdl.getString("SETTINGS.NETWORK.PANEL.INPUT_NODE_URL"), null, JOptionPane.INFORMATION_MESSAGE, null, null, o.getUrl());
			if (str == null) {
				return;
			}
			try {
				new URL(str.toString());
			} catch (Throwable x) {
				return;
			}
			o.setUrl(str.toString());
			MyDb.update_network(o);
			EventBus.getDefault().post(new NetworkChangeEvent());
		});
		return panel;
	}

	private static final void createWeb3jAuthDialog(Component c) {
		var w = SwingUtilities.getWindowAncestor(c);
		var panel_1 = new JPanel(new GridBagLayout());
		panel_1.add(new JLabel(rsc_bdl.getString("SETTINGS.NETWORK.PANEL.PROJECT_ID")), new GridBagConstraints(0, 0, 1, 1, 0, 0, 17, 0, new Insets(0, 5, 5, 5), 0, 0));
		var id_field = new JTextField("<Your ID here>", 30);
		panel_1.add(id_field, new GridBagConstraints(0, 1, 1, 1, 0, 0, 17, 0, new Insets(0, 5, 5, 5), 0, 0));
		panel_1.add(new JLabel(rsc_bdl.getString("SETTINGS.NETWORK.PANEL.PROJECT_SECERT")), new GridBagConstraints(0, 2, 1, 1, 0, 0, 17, 0, new Insets(0, 5, 5, 0), 0, 0));
		var scret_field = new JPasswordField("unchanged", 30);
		panel_1.add(scret_field, new GridBagConstraints(0, 3, 1, 1, 0, 0, 17, 0, new Insets(0, 5, 5, 5), 0, 0));
		MyDb.get_webj_auth().ifPresent(r -> id_field.setText(r.getStr("MYAUTH")));

		int i = JOptionPane.showConfirmDialog(w, panel_1, rsc_bdl.getString("SETTINGS.NETWORK.PANEL.INPUT_PROJECT_INFO"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, UIUtil.getStretchIcon("icon/" + "key_1.svg", 64, 64));
		if (i != JOptionPane.OK_OPTION) {
			return;
		}
		boolean b = MyDb.update_webj_auth(id_field.getText(), new String(scret_field.getPassword()));
		if (b) {
			CryptoUtil.clear_web3j();
		}
	}

}
