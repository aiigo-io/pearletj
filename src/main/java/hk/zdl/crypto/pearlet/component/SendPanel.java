package hk.zdl.crypto.pearlet.component;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JLayer;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import org.bouncycastle.util.encoders.Base64;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;

import hk.zdl.crypto.pearlet.component.event.AccountChangeEvent;
import hk.zdl.crypto.pearlet.component.event.BalanceUpdateEvent;
import hk.zdl.crypto.pearlet.ds.CryptoNetwork;
import hk.zdl.crypto.pearlet.persistence.MyDb;
import hk.zdl.crypto.pearlet.tx.SendTxFactory;
import hk.zdl.crypto.pearlet.ui.SpinableIcon;
import hk.zdl.crypto.pearlet.ui.UIUtil;
import hk.zdl.crypto.pearlet.ui.WaitLayerUI;
import hk.zdl.crypto.pearlet.util.CryptoUtil;
import hk.zdl.crypto.pearlet.util.Util;
import signumj.entity.response.Asset;

@SuppressWarnings("serial")
public class SendPanel extends JPanel {

	private static final Dimension FIELD_DIMENSION = new Dimension(500, 20);
	private static final ResourceBundle rsc_bdl = Util.getResourceBundle();
	private final JPanel panel_2 = new JPanel(new BorderLayout());
	private final JPanel fee_panel = new JPanel(new GridLayout(1, 0));
	private final JTextField fee_field = new JTextField("");
	private final JSlider fee_slider = new JSlider();
	private final JLayer<JPanel> jlayer = new JLayer<>();
	private final JLabel fee_label = new JLabel(rsc_bdl.getString("GENERAL.FEE"));
	private final WaitLayerUI wuli = new WaitLayerUI();
	private final JComboBox<String> acc_combo_box = new JComboBox<>();
	private final JComboBox<Object> token_combo_box = new JComboBox<>();
	private final Map<Object, BigDecimal> asset_balance = new HashMap<>();
	private final JLabel balance_label = new JLabel();
	private final SpinableIcon busy_icon = new SpinableIcon(new BufferedImage(32, 32, BufferedImage.TYPE_4BYTE_ABGR), 32, 32);
	private boolean refresh_lock = false;
	private JButton send_btn;
	private CryptoNetwork network;
	private String account;
	private int decimalPlaces = 8;

	public SendPanel() {
		super(new BorderLayout());
		EventBus.getDefault().register(this);
		if (System.getProperty("os.name").toLowerCase().contains("mac")) {
			getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.META_DOWN_MASK), "macRefresh");
			getActionMap().put("macRefresh", new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (network != null && account != null) {
						if(!refresh_lock) {
							EventBus.getDefault().post(new AccountChangeEvent(network, account));
						}
					}
				}
			});
		}
		add(jlayer, BorderLayout.CENTER);
		var _panel = new JPanel(new FlowLayout());
		jlayer.setView(_panel);
		jlayer.setUI(wuli);
		var panel_1 = new JPanel(new GridBagLayout());
		_panel.add(panel_1);
		var label_1 = new JLabel(rsc_bdl.getString("GENERAL.ACCOUNT"));
		panel_1.add(label_1, newGridConst(0, 0, 3, 17));
		var label_2 = new JLabel(rsc_bdl.getString("SEND_PANEL.BALANCE"));
		label_2.setPreferredSize(new Dimension(100, 20));
		panel_1.add(label_2, newGridConst(3, 0, 1, 17));
		var label_3 = new JLabel(rsc_bdl.getString("SEND_PANEL.TOKEN"));
		label_3.setPreferredSize(new Dimension(100, 20));
		panel_1.add(label_3, newGridConst(4, 0, 1, 17));
		acc_combo_box.setPreferredSize(new Dimension(300, 20));
		panel_1.add(acc_combo_box, newGridConst(0, 1, 3));
		panel_1.add(balance_label, newGridConst(3, 1, 1, 13));
		token_combo_box.setPreferredSize(new Dimension(100, 20));
		panel_1.add(token_combo_box, newGridConst(4, 1, 1, 13));

		var label_4 = new JLabel(rsc_bdl.getString("SEND_PANEL.RECP"));
		panel_1.add(label_4, newGridConst(0, 2, 3, 17));
		var rcv_field = new JTextField();
		rcv_field.setPreferredSize(FIELD_DIMENSION);
		panel_1.add(rcv_field, newGridConst(0, 3, 5));

		Stream.of(acc_combo_box, rcv_field).forEach(o -> o.setFont(new Font(Font.MONOSPACED, Font.PLAIN, getFont().getSize())));

		var label_5 = new JLabel(rsc_bdl.getString("SEND_PANEL.AMOUNT"));
		panel_1.add(label_5, newGridConst(0, 4, 3, 17));
		var amt_field = new JTextField();
		amt_field.setPreferredSize(FIELD_DIMENSION);
		panel_1.add(amt_field, newGridConst(0, 5, 5));

		panel_1.add(fee_label, newGridConst(0, 6, 3, 17));
		fee_panel.setPreferredSize(FIELD_DIMENSION);
		Stream.of(fee_field, fee_slider).forEach(fee_panel::add);
		fee_field.setEditable(false);
		fee_slider.addChangeListener(e -> fee_field.setText(new BigDecimal(fee_slider.getValue()).movePointLeft(decimalPlaces).toPlainString()));
		panel_1.add(fee_panel, newGridConst(0, 7, 5, 17));

		var panel_3 = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		var msg_chk_box = new JCheckBox(rsc_bdl.getString("SEND_PANEL.ADDMSG"));
		msg_chk_box.setPreferredSize(new Dimension(150, 30));
		panel_3.add(msg_chk_box);
		var msg_option_btn = new JButton("↓");
		var msg_option_popup = new JPopupMenu();
		var eny_msg_menu_item = new JCheckBoxMenuItem(rsc_bdl.getString("SEND_PANEL.ENC"));
		msg_option_popup.add(eny_msg_menu_item);
		var eny_msg_sub_menu = new JMenu(rsc_bdl.getString("SEND_PANEL.SEND_AS"));
		msg_option_popup.add(eny_msg_sub_menu);
		var plain_text_option_menu_item = new JRadioButtonMenuItem(rsc_bdl.getString("SEND_PANEL.PLAIN_TEXT"), true);
		eny_msg_sub_menu.add(plain_text_option_menu_item);
		var base64_option_menu_item = new JRadioButtonMenuItem("Base64", false);
		eny_msg_sub_menu.add(base64_option_menu_item);
		var btn_gp_1 = new ButtonGroup();
		Stream.of(plain_text_option_menu_item, base64_option_menu_item).forEach(btn_gp_1::add);

		msg_option_btn.addActionListener((e) -> msg_option_popup.show(msg_option_btn, 0, 0));
		panel_3.add(msg_option_btn);

		var label_7 = new JLabel("0/1000");
		label_7.setHorizontalAlignment(SwingConstants.RIGHT);
		panel_2.add(label_7, BorderLayout.EAST);
		panel_2.add(panel_3, BorderLayout.CENTER);
		panel_2.setPreferredSize(new Dimension(500, 40));
		panel_1.add(panel_2, newGridConst(0, 8, 5, 17));

		var msg_area = new JTextArea();
		var msg_scr = new JScrollPane(msg_area);
		msg_scr.setPreferredSize(new Dimension(500, 200));
		panel_1.add(msg_scr, newGridConst(0, 9, 5));

		send_btn = new JButton(rsc_bdl.getString("SEND_PANEL.BTN_TEXT"), UIUtil.getStretchIcon("toolbar/paper-plane-solid.svg", 32, 32));
		send_btn.setFont(new Font("Arial Black", Font.PLAIN, 32));
		send_btn.setMultiClickThreshhold(300);
		send_btn.setEnabled(false);

		var send_key_listener = new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					send_btn.doClick();
				}
			}
		};
		Stream.of(amt_field, rcv_field).forEach(o -> o.addKeyListener(send_key_listener));

		try {
			var btn_img = ImageIO.read(Util.getResource("icon/spinner-solid.svg"));
			busy_icon.setImage(btn_img, 32, 32);
			send_btn.setDisabledIcon(busy_icon);
		} catch (IOException x) {
			Logger.getLogger(getClass().getName()).log(Level.WARNING, x.getMessage(), x);
		}
		panel_1.add(send_btn, new GridBagConstraints(0, 12, 5, 1, 0, 0, 10, 0, new Insets(5, 0, 5, 0), 0, 0));

		token_combo_box.addActionListener(e -> update_balance_label(asset_balance.get(token_combo_box.getSelectedItem())));
		token_combo_box.setRenderer(new DefaultListCellRenderer() {

			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				if (value instanceof Asset) {
					value = ((Asset) value).getName();
				} else if (value instanceof JSONObject) {
					value = ((JSONObject) value).getString("contract_name");
				}
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				return this;
			}
		});

		SwingUtilities.invokeLater(() -> {
			Stream.of(msg_option_btn, label_7, msg_scr).forEach(o -> o.setVisible(false));
			msg_chk_box.addActionListener((e) -> Stream.of(msg_option_btn, label_7, msg_scr).forEach(o -> o.setVisible(msg_chk_box.isSelected())));
			msg_area.addKeyListener(new KeyListener() {

				@Override
				public void keyTyped(KeyEvent e) {
				}

				@Override
				public void keyPressed(KeyEvent e) {
				}

				@Override
				public void keyReleased(KeyEvent e) {
					int i = msg_area.getText().getBytes().length;
					label_7.setText(i + "/" + "1000");
					label_7.setForeground(i > 1000 ? Color.red : label_1.getForeground());
				}
			});
		});
		send_btn.addActionListener(e -> {
			if (rcv_field.getText().isBlank()) {
				JOptionPane.showMessageDialog(getRootPane(), rsc_bdl.getString("SEND_PANEL.RECP_EMPTY"), rsc_bdl.getString("GENERAL.ERROR"), JOptionPane.INFORMATION_MESSAGE);
				return;
			} else {
				if (!CryptoUtil.isValidAddress(network, rcv_field.getText())) {
					JOptionPane.showMessageDialog(getRootPane(), rsc_bdl.getString("SEND_PANEL.INVALID_ADDRESS"), rsc_bdl.getString("GENERAL.ERROR"), JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
			BigDecimal amount;
			try {
				amount = new BigDecimal(amt_field.getText());
				if (amount.doubleValue() <= 0) {
					throw new IllegalArgumentException();
				}
			} catch (Exception x) {
				JOptionPane.showMessageDialog(getRootPane(), rsc_bdl.getString("SEND_PANEL.INVALID_AMOUNT"), rsc_bdl.getString("GENERAL.ERROR"), JOptionPane.ERROR_MESSAGE);
				return;
			}
			String asset_id = null;
			if (network.isBurst()) {
				if (token_combo_box.getSelectedIndex() > 0) {
					Object o = token_combo_box.getSelectedItem();
					if (o instanceof Asset) {
						Asset a = (Asset) o;
						asset_id = a.getAssetId().getID();
						amount = amount.movePointRight(a.getDecimals());
					}
				}
			} else if (network.isWeb3J()) {
				Object o = token_combo_box.getSelectedItem();
				if (o instanceof JSONObject) {
					var jobj = (JSONObject) o;
					asset_id = jobj.getString("contract_address");
					int decimals = jobj.getInt("contract_decimals");
					// 添加余额验证
					BigDecimal balance = new BigDecimal(jobj.getString("balance")).movePointLeft(decimals);
					if (amount.compareTo(balance) > 0) {
						JOptionPane.showMessageDialog(getRootPane(), rsc_bdl.getString("SEND_PANEL.INSUFFICIENT_BALANCE"), rsc_bdl.getString("GENERAL.ERROR"), JOptionPane.ERROR_MESSAGE);
						return;
					}
				}
			}

			var send_tx = SendTxFactory.createSendTx(network, account, rcv_field.getText(), amount, new BigDecimal(fee_field.getText()), asset_id);
			if (msg_chk_box.isSelected()) {
				send_tx.setEncrypted(eny_msg_menu_item.isSelected());
				if (plain_text_option_menu_item.isSelected()) {
					String str = msg_area.getText().trim();
					if (str.getBytes().length > 1000) {
						JOptionPane.showMessageDialog(getRootPane(), rsc_bdl.getString("SEND_PANEL.MSG_2_LONG"), rsc_bdl.getString("GENERAL.ERROR"), JOptionPane.ERROR_MESSAGE);
						return;
					}
					send_tx.setMessage(str);
				} else if (base64_option_menu_item.isSelected()) {
					byte[] bArr;
					try {
						bArr = Base64.decode(msg_area.getText().trim());
					} catch (Exception x) {
						JOptionPane.showMessageDialog(getRootPane(), rsc_bdl.getString("SEND_PANEL.ERR_1"), rsc_bdl.getString("GENERAL.ERROR"), JOptionPane.ERROR_MESSAGE);
						return;
					}
					if (bArr.length > 1000) {
						JOptionPane.showMessageDialog(getRootPane(), rsc_bdl.getString("SEND_PANEL.MSG_2_LONG"), rsc_bdl.getString("GENERAL.ERROR"), JOptionPane.ERROR_MESSAGE);
						return;
					}
					send_tx.setMessage(bArr);
				}
			}
			msg_area.setEnabled(false);
			send_btn.setEnabled(false);
			busy_icon.start();
			Util.submit(() -> {
				var b = false;
				try {
					if (send_tx.call()) {
						b = true;
					} else {
						JOptionPane.showMessageDialog(getRootPane(), rsc_bdl.getString("SEND_PANEL.FAIL_1"), rsc_bdl.getString("GENERAL.ERROR"), JOptionPane.ERROR_MESSAGE);
					}
				} catch (Throwable x) {
					while (x.getCause() != null) {
						x = x.getCause();
					}
					Logger.getLogger(getClass().getName()).log(Level.WARNING, x.getMessage(), x);
					while (x.getClass().equals(java.util.concurrent.ExecutionException.class) && x.getCause() != null) {
						x = x.getCause();
					}
					JOptionPane.showMessageDialog(getRootPane(), x.getMessage(), x.getClass().getName(), JOptionPane.ERROR_MESSAGE);
					return null;
				} finally {
					busy_icon.stop();
					send_btn.setEnabled(true);
					msg_area.setEnabled(true);
				}
				if (b) {
					UIUtil.displayMessage(rsc_bdl.getString("SEND_PANEL.TITLE"), rsc_bdl.getString("SEND_PANEL.DONE_MSG"));
					var old = balance_label.getText();
					var acc = account;
					for (var i = 0; i < 5; i++) {
						TimeUnit.SECONDS.sleep(2);
						update_balance();
						var now = balance_label.getText();
						if (acc != account || !now.equals(old)) {
							break;
						}
					}
				} else {
					JOptionPane.showMessageDialog(getRootPane(), rsc_bdl.getString("SEND_PANEL.FAIL_2"), rsc_bdl.getString("GENERAL.ERROR"), JOptionPane.ERROR_MESSAGE);
				}
				return null;
			});

		});
	}

	@Subscribe(threadMode = ThreadMode.ASYNC)
	public synchronized void onMessage(AccountChangeEvent e) {
		var network_change = this.network != e.network;
		this.network = e.network;
		this.account = e.account;
		var symbol = "";
		if (network == null) {
			return;
		} else if (network.isBurst()) {
			try {
				symbol = CryptoUtil.getConstants(network).getString("valueSuffix");
			} catch (Exception x) {
			}
		}
		balance_label.setText("?");
		balance_label.setToolTipText(null);
		token_combo_box.setModel(new DefaultComboBoxModel<Object>(new String[] { symbol }));
		acc_combo_box.setModel(new DefaultComboBoxModel<String>(new String[] { account }));
		asset_balance.clear();
		if (network == null || account == null || account.isBlank()) {
			balance_label.setText("0");
			send_btn.setEnabled(false);
		} else {
			wuli.start();
			refresh_lock = true;
			if (network_change || fee_field.getText().isBlank()) {
				Util.submit(() -> {
					decimalPlaces = CryptoUtil.getConstants(network).getInt("decimalPlaces");
					var g = CryptoUtil.getFeeSuggestion(network);
					// 将滑动条属性设置操作放到 EDT 中执行
					SwingUtilities.invokeLater(() -> {
						fee_slider.setMinimum(g.getCheapFee().toNQT().intValue());
						fee_slider.setMaximum(g.getPriorityFee().toNQT().intValue());
						fee_slider.setValue(g.getStandardFee().toNQT().intValue());
					});
					return null;
				});
			}
			Util.submit(() -> {
				try {
					update_balance();
				} catch (Throwable x) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, x.getMessage(), x);
				} finally {
					wuli.stop();
					refresh_lock = false;
				}
			});
			var o_b = MyDb.isWatchAccount(network, account);
			if (o_b.isPresent() && o_b.get() == false) {
				send_btn.setEnabled(true);
			} else {
				send_btn.setEnabled(false);
			}
		}
		Stream.of(panel_2, fee_panel, fee_label).forEach(c -> c.setVisible(network.isBurst()));
	}

	private final void update_balance() throws Exception {
		var symbol = "";
		if (network.isBurst()) {
			symbol = CryptoUtil.getConstants(network).getString("valueSuffix");
			var account = CryptoUtil.getAccount(network, this.account);
			var balance = account.getBalance();
			var committed_balance = account.getCommittedBalance();
			balance = balance.subtract(committed_balance);
			var value = new BigDecimal(balance.toNQT(), decimalPlaces);
			asset_balance.put(symbol, value);
			EventBus.getDefault().post(new BalanceUpdateEvent(network, this.account, value));
			for (var ab : account.getAssetBalances()) {
				var a = CryptoUtil.getAsset(network, ab.getAssetId().toString());
				var val = new BigDecimal(a.getQuantity().toNQT()).movePointLeft(a.getDecimals());
				asset_balance.put(a, val);
				((DefaultComboBoxModel<Object>) token_combo_box.getModel()).addElement(a);
			}
		} else if (network.isWeb3J()) {
			try {
				var value = CryptoUtil.getBalance(network, account);
				asset_balance.put(symbol, value);
				EventBus.getDefault().post(new BalanceUpdateEvent(network, this.account, value));
			} catch (Exception x) {
				Logger.getLogger(getClass().getName()).log(Level.WARNING, x.getMessage(), x);
			}
			Optional<JSONArray> o_arr = MyDb.getETHTokenList(account);
			if (o_arr.isPresent()) {
				var jarr = o_arr.get();
				var arr = new JSONObject[jarr.length()];
				int j = -1;
				for (int i = 0; i < arr.length; i++) {
					var jobj = jarr.getJSONObject(i);
					arr[i] = jobj;
					var val = new BigDecimal(jobj.getString("balance")).movePointLeft(jobj.getInt("contract_decimals"));
					asset_balance.put(jobj, val);
					if (jobj.getString("contract_name").equals("Ether") && jobj.getString("contract_ticker_symbol").equals("ETH")) {
						j = i;
					}
				}
				token_combo_box.setModel(new DefaultComboBoxModel<Object>(arr));
				token_combo_box.setSelectedIndex(j);
			}
		}
	}

	@Subscribe(threadMode = ThreadMode.ASYNC)
	public void onMessage(BalanceUpdateEvent e) {
		if (e.getNetwork().equals(network) && e.getAddress().equals(account)) {
			update_balance_label(e.getBalance());
		}
	}

	private final void update_balance_label(BigDecimal value) {
		balance_label.setText(value.setScale(2, RoundingMode.HALF_UP).toPlainString());
	}

	private static final GridBagConstraints newGridConst(int x, int y, int width) {
		var a = new GridBagConstraints();
		a.gridx = x;
		a.gridy = y;
		a.gridwidth = width;
		return a;
	}

	private static final GridBagConstraints newGridConst(int x, int y, int width, int anchor) {
		var a = new GridBagConstraints();
		a.gridx = x;
		a.gridy = y;
		a.gridwidth = width;
		a.anchor = anchor;
		return a;
	}

}
