package hk.zdl.crypto.pearlet.component;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayer;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;
import org.web3j.utils.Convert;

import hk.zdl.crypto.pearlet.component.dashboard.TxProc;
import hk.zdl.crypto.pearlet.component.dashboard.TxTableModel;
import hk.zdl.crypto.pearlet.component.event.AccountChangeEvent;
import hk.zdl.crypto.pearlet.component.event.BalanceUpdateEvent;
import hk.zdl.crypto.pearlet.component.event.TxHistoryEvent;
import hk.zdl.crypto.pearlet.ds.AltTokenWrapper;
import hk.zdl.crypto.pearlet.ds.CryptoNetwork;
import hk.zdl.crypto.pearlet.persistence.MyDb;
import hk.zdl.crypto.pearlet.ui.UIUtil;
import hk.zdl.crypto.pearlet.ui.WaitLayerUI;
import hk.zdl.crypto.pearlet.util.CryptoUtil;
import hk.zdl.crypto.pearlet.util.Util;

@SuppressWarnings("serial")
public class DashBoard extends JPanel {

	private static Font title_font = new Font("Arial", Font.BOLD, 16);
	private static Font asset_box_font = new Font("Arial", Font.PLAIN, 16);
	private final ResourceBundle rsc_bdl = Util.getResourceBundle();
	private final JList<AltTokenWrapper> token_list = new JList<>();
	private final JScrollPane token_list_scr_pane = new JScrollPane(token_list, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	private final JProgressBar token_list_progress_bar = new JProgressBar(JProgressBar.VERTICAL);
	private final CardLayout token_list_card_layout = new CardLayout();
	private final JPanel asset_info_panel = new JPanel(new GridLayout(0, 1));
	private final JPanel token_list_panel = new JPanel(token_list_card_layout);
	private final JLabel currency_label = new JLabel(), balance_label = new JLabel();
	private final TxTableModel table_model = new TxTableModel();
	private final TableColumnModel table_column_model = new DefaultTableColumnModel();
	private final JTable table = new JTable(table_model, table_column_model);
	private final JScrollPane table_scroll_pane = new JScrollPane(table);
	private final WaitLayerUI wuli = new WaitLayerUI();
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private final JLayer<JScrollPane> scroll_pane_layer = new JLayer(table_scroll_pane, wuli);
	private final JButton manage_token_list_btn = new JButton(rsc_bdl.getString("DASHBOARD.MANAGE_TOKEN_LIST"));
	private boolean refresh_lock = false;
	private long _last_table_update;
	private CryptoNetwork network;
	private String account;
	private Thread token_list_thread = null;

	public DashBoard() {
		super(new GridBagLayout());
		EventBus.getDefault().register(this);
		if (System.getProperty("os.name").toLowerCase().contains("mac")) {
			getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.META_DOWN_MASK), "macRefresh");
			getActionMap().put("macRefresh", new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (network != null && account != null) {
						if (!refresh_lock) {
							EventBus.getDefault().post(new AccountChangeEvent(network, account));
						}
					}
				}
			});
		}
		var label1 = new JLabel(rsc_bdl.getString("DASHBOARD.TITLE_TOKENS"));
		add(label1, new GridBagConstraints(0, 0, 1, 1, 0, 0, 17, 1, new Insets(0, 20, 0, 0), 0, 0));
		token_list_progress_bar.setIndeterminate(true);
		token_list_panel.setPreferredSize(new Dimension(200, 300));
		token_list_panel.add("list", token_list_scr_pane);
		token_list_panel.add("bar", token_list_progress_bar);
		token_list_card_layout.show(token_list_panel, "list");
		add(token_list_panel, new GridBagConstraints(0, 1, 1, 2, 0, 1, 17, 1, new Insets(0, 0, 0, 0), 0, 0));
		add(manage_token_list_btn, new GridBagConstraints(0, 3, 1, 1, 0, 0, 10, 2, new Insets(5, 5, 5, 5), 0, 0));
		var manage_token_list_menu = new JPopupMenu();
		var issue_token_menu_item = new JMenuItem(rsc_bdl.getString("DASHBOARD.MANAGE_ISSUE_TOKEN"));
		manage_token_list_menu.add(issue_token_menu_item);
		manage_token_list_btn.addActionListener(e -> manage_token_list_menu.show(manage_token_list_btn, 0, 0));
		issue_token_menu_item.addActionListener(e -> Stream.of(new IssueTokenPanel(getRootPane(), network, account).showConfirmDialog()).filter(Boolean::valueOf).findAny()
				.ifPresent(o -> EventBus.getDefault().post(new AccountChangeEvent(network, account))));

		var label2 = new JLabel(rsc_bdl.getString("DASHBOARD.TITLE_BALANCE"));
		add(label2, new GridBagConstraints(1, 0, 1, 1, 0, 0, 17, 0, new Insets(0, 20, 0, 0), 0, 0));
		var balance_inner_panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		Stream.of(label1, label2, balance_label).forEach(o -> o.setFont(title_font));
		currency_label.setFont(new Font(Font.MONOSPACED, title_font.getStyle(), title_font.getSize()));
		Stream.of(currency_label, balance_label).forEach(balance_inner_panel::add);
		add(balance_inner_panel, new GridBagConstraints(2, 0, 1, 1, 1, 0, 13, 0, new Insets(0, 0, 0, 0), 0, 0));
		token_list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		var panel_1 = new JPanel(new BorderLayout());
		var panel_3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		var panel_4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		var asset_balance_label = new JLabel();
		var asset_name_label = new JLabel();
		var asset_id_label_0 = new JLabel();
		var asset_id_label_1 = new JLabel();
		panel_3.add(asset_balance_label);
		panel_3.add(asset_name_label);
		panel_4.add(asset_id_label_0);
		panel_4.add(asset_id_label_1);
		asset_info_panel.add(panel_3);
		asset_info_panel.add(panel_4);
		panel_1.add(asset_info_panel, BorderLayout.NORTH);
		asset_info_panel.setVisible(false);
		Stream.of(asset_balance_label, asset_name_label).forEach(o -> o.setFont(asset_box_font));
		token_list.setCellRenderer(new DefaultListCellRenderer() {

			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				var atw = (AltTokenWrapper) value;
				if (atw.network.isBurst()) {
					if (atw.asset != null) {
						return super.getListCellRendererComponent(list, atw.asset.getName(), index, isSelected, cellHasFocus);
					}
				} else if (atw.network.isWeb3J()) {
					String name = atw.jobj.optString("contract_name");
					if (!name.isBlank()) {
						return super.getListCellRendererComponent(list, name, index, isSelected, cellHasFocus);
					}
				}
				return super.getListCellRendererComponent(list, "", index, isSelected, cellHasFocus);
			}
		});

		token_list.addListSelectionListener(e -> {
			if (token_list.getSelectedIndex() < 0) {
				asset_info_panel.setVisible(false);
			} else {
				var atw = token_list.getSelectedValue();
				if (atw.network.isBurst()) {
					var a = atw.asset;
					asset_id_label_0.setText("asset id:");
					asset_id_label_1.setText(a.getAssetId().getID());
					var desc = a.getDescription();
					asset_info_panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createDashedBorder(getForeground()), desc, TitledBorder.LEFT, TitledBorder.TOP, asset_box_font));
					asset_name_label.setText(a.getName());
					BigDecimal val = new BigDecimal(a.getQuantity().toNQT()).movePointLeft(a.getDecimals());
					asset_balance_label.setText(val.toString());
					asset_info_panel.setVisible(true);
				} else if (atw.network.isWeb3J()) {
					var jobj = atw.jobj;
					var contract_name = jobj.optString("contract_name");
					var contract_ticker_symbol = jobj.optString("contract_ticker_symbol");
					asset_id_label_0.setText("contract address:");
					asset_id_label_1.setText(jobj.optString("contract_address"));
					var desc = contract_name;
					asset_info_panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createDashedBorder(getForeground()), desc, TitledBorder.LEFT, TitledBorder.TOP, asset_box_font));
					asset_name_label.setText(contract_ticker_symbol);
					BigDecimal val = new BigDecimal(jobj.getString("balance")).movePointLeft(jobj.getInt("contract_decimals"));
					asset_balance_label.setText(val.stripTrailingZeros().toPlainString());
					asset_info_panel.setVisible(true);
				}
			}
		});
		token_list.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent mouseEvent) {
				if (mouseEvent.getClickCount() == 2 && token_list.getSelectedIndex() > -1) {
					var atw = token_list.getSelectedValue();
					if (atw.network.isWeb3J()) {
						var jobj = atw.jobj;
						try {
							Util.viewContractDetail(network, jobj);
						} catch (Exception x) {
							Logger.getLogger(getClass().getName()).log(Level.WARNING, x.getMessage(), x);
						}
					}
				}
			}
		});

		for (int i = 0; i < table_model.getColumnCount(); i++) {
			var tc = new TableColumn(i, 0);
			tc.setHeaderValue(table_model.getColumnName(i));
			table_column_model.addColumn(tc);
		}
		table.setFont(new Font(Font.MONOSPACED, Font.PLAIN, getFont().getSize()));
		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setResizingAllowed(false);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setShowGrid(true);
		UIUtil.adjust_table_width(table, table_column_model);
		add(panel_1, new GridBagConstraints(1, 1, 2, 3, 1, 1, 10, 1, new Insets(0, 0, 0, 0), 0, 0));
		panel_1.add(scroll_pane_layer, BorderLayout.CENTER);
		table_model.addTableModelListener(e -> UIUtil.adjust_table_width(table, table_column_model));
		table.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent mouseEvent) {
				Point point = mouseEvent.getPoint();
				int row = table.rowAtPoint(point);
				if (mouseEvent.getClickCount() == 2 && row >= 0 & row == table.getSelectedRow()) {
					try {
						Util.viewTxDetail(network, table_model.getValueAt(row, 0));
					} catch (Exception x) {
						Logger.getLogger(getClass().getName()).log(Level.WARNING, x.getMessage(), x);
					}
				}
			}
		});

	}

	@Subscribe(threadMode = ThreadMode.ASYNC)
	public synchronized void onMessage(AccountChangeEvent e) {
		this.network = e.network;
		this.account = e.account;
		var symbol = "";
		currency_label.setText(symbol);
		token_list_card_layout.show(token_list_panel, "bar");
		token_list.setModel(new DefaultComboBoxModel<AltTokenWrapper>());
		asset_info_panel.setVisible(false);
		if (network == null || account == null || account.isBlank()) {
			balance_label.setText("0");
			token_list_card_layout.show(token_list_panel, "list");
		} else {
			balance_label.setText("?");
			new TxProc().update_column_model(e.network, table_column_model, e.account);
			if (network.isBurst()) {
				Util.submit(() -> {
					try {
						var account = CryptoUtil.getAccount(network, e.account);
						var balance_text = "?";
						var balance = account.getBalance();
						var committed_balance = account.getCommittedBalance();
						balance = balance.subtract(committed_balance);
						var decimalPlaces = CryptoUtil.getConstants(network).getInt("decimalPlaces");
						balance_text = new BigDecimal(balance.toNQT(), decimalPlaces).setScale(2, RoundingMode.HALF_UP).toPlainString(); // 保留两位小数（四舍五入）
						EventBus.getDefault().post(new BalanceUpdateEvent(network, e.account, new BigDecimal(balance_text)));
						token_list.setListData(Arrays.asList(account.getAssetBalances()).stream().map(o -> CryptoUtil.getAsset(network, o.getAssetId().toString()))
								.map(o -> new AltTokenWrapper(network, o)).toArray((i) -> new AltTokenWrapper[i]));
						if (token_list.getModel().getSize() > 0) {
							token_list.setSelectedIndex(0);
						} else {
							token_list.setSelectedIndex(-1);
						}
					} catch (Exception x) {
						Logger.getLogger(getClass().getName()).log(Level.WARNING, x.getMessage(), x);
					} finally {
						token_list_card_layout.show(token_list_panel, "list");
						updateUI();
					}
				});
			} else if (network.isWeb3J()) {
				Util.submit(() -> {
					try {
						var balance = CryptoUtil.getBalance(network, account);
						EventBus.getDefault().post(new BalanceUpdateEvent(network, account, balance));
					} catch (Exception x) {
						Logger.getLogger(getClass().getName()).log(Level.WARNING, x.getMessage(), x);
					} finally {
						updateUI();
					}
				});
				refresh_token_list();
			}
		}
		manage_token_list_btn.setEnabled(network != null && network.isBurst());
	}

	private final synchronized void refresh_token_list() {
		// 检查旧线程是否已结束，避免重复启动
		if (token_list_thread != null && token_list_thread.isAlive()) {
			return;
		}
		token_list_thread = new Thread() {

			@Override
			public void run() {
				try {
					Optional<JSONArray> o_arr = MyDb.getETHTokenList(account);
					if (o_arr.isPresent()) {
						update_token_list(o_arr.get());
					}
					var items = CryptoUtil.getAccountBalances(account);
					for (int i = 0; i < items.length(); i++) {
						var jobj = items.getJSONObject(i);
						if (jobj.getString("contract_name").equals("Ether") && jobj.getString("contract_ticker_symbol").equals("ETH")) {
							BigInteger wei = new BigInteger(jobj.getString("balance"));
							BigDecimal eth = Convert.fromWei(new BigDecimal(wei), Convert.Unit.ETHER);
							SwingUtilities.invokeLater(() -> {
								balance_label.setText(eth.setScale(2, RoundingMode.HALF_UP).toPlainString());
							});
							break;
						}
					}
					update_token_list(items);
					MyDb.putETHTokenList(account, items);
				} catch (Exception x) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, x.getMessage(), x);
				} finally {
					token_list_card_layout.show(token_list_panel, "list");
					updateUI();
				}

			}

			private void update_token_list(JSONArray items) {
				List<AltTokenWrapper> l = new ArrayList<>(items.length());
				for (int i = 0; i < items.length(); i++) {
					var jobj = items.getJSONObject(i);
					l.add(new AltTokenWrapper(network, jobj));
				}
				int i = token_list.getLeadSelectionIndex();
				token_list.setListData(l.toArray(new AltTokenWrapper[items.length()]));
				token_list.setSelectedIndex(Math.min(i, items.length()));
				token_list_card_layout.show(token_list_panel, "list");
			}
		};
		token_list_thread.setPriority(Thread.MIN_PRIORITY);
		token_list_thread.setDaemon(true);
		token_list_thread.start();
	}

	@Subscribe(threadMode = ThreadMode.ASYNC)
	public void onMessage(BalanceUpdateEvent e) {
		String balance = e.getBalance().setScale(2, RoundingMode.HALF_UP).toPlainString();
		if (e.getNetwork().equals(network) && e.getAddress().equals(account)) {
			balance_label.setText(balance);
		}
	}

	@Subscribe(threadMode = ThreadMode.ASYNC)
	public void onMessage(TxHistoryEvent<?> e) {
		switch (e.type) {
		case START:
			refresh_lock = true;
			wuli.start();
			table_model.clearData();
			break;
		case FINISH:
			refresh_lock = false;
			wuli.stop();
			break;
		case INSERT:
			Object o = e.data;
			table_model.insertData(new Object[] { o, o, o, o, o });
			if (System.currentTimeMillis() - _last_table_update > 1000) {
				SwingUtilities.invokeLater(() -> {
					table.updateUI();
					_last_table_update = System.currentTimeMillis();
				});
			}
			Util.submit(() -> check_data(o, table_model.getRowCount(), 3));
			break;
		default:
			break;

		}
	}

	private Void check_data(Object aValue, int rowIndex, int columnIndex) throws Exception {
		var tx = (JSONObject) aValue;
		var type = tx.getJSONArray("transaction_types").toList().stream().map(Object::toString).toList();
		if (type.contains("token_transfer")) {
			var x = CryptoUtil.getTxAmount(network, tx.getString("hash"));
			tx.put("text", x.setScale(2, RoundingMode.HALF_UP).toPlainString()); // 保留两位小数（四舍五入）
			SwingUtilities.invokeLater(() -> table_model.setValueAt(tx, rowIndex, columnIndex));
		}
		return null;
	}

}
