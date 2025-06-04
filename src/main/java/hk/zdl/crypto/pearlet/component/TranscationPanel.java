package hk.zdl.crypto.pearlet.component;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.JLayer;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import hk.zdl.crypto.pearlet.component.dashboard.TxProc;
import hk.zdl.crypto.pearlet.component.dashboard.TxTableModel;
import hk.zdl.crypto.pearlet.component.event.AccountChangeEvent;
import hk.zdl.crypto.pearlet.component.event.TxHistoryEvent;
import hk.zdl.crypto.pearlet.ds.CryptoNetwork;
import hk.zdl.crypto.pearlet.ui.UIUtil;
import hk.zdl.crypto.pearlet.ui.WaitLayerUI;
import hk.zdl.crypto.pearlet.util.Util;

public class TranscationPanel extends JPanel {
	private static final long serialVersionUID = -3430209712761314513L;
	private final JLayer<JPanel> jlayer = new JLayer<>();
	private final WaitLayerUI wuli = new WaitLayerUI();
	private final TxTableModel table_model = new TxTableModel();
	private final TableColumnModel table_column_model = new DefaultTableColumnModel();
	private final JTable table = new JTable(table_model, table_column_model);
	private boolean refresh_lock = false;
	private long _last_table_update;
	private CryptoNetwork network;
	private String account;

	public TranscationPanel() {
		super(new BorderLayout());
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
		add(jlayer, BorderLayout.CENTER);
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
		var _panel = new JPanel(new BorderLayout());
		_panel.add(new JScrollPane(table), BorderLayout.CENTER);
		jlayer.setView(_panel);
		jlayer.setUI(wuli);

		UIUtil.adjust_table_width(table, table_column_model);
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
	public void onMessage(AccountChangeEvent e) {
		this.network = e.network;
		this.account = e.account;
		if (network == null || e.account == null || e.account.isBlank()) {
			return;
		}
		new TxProc().update_column_model(e.network, table_column_model, e.account);

	}

	@Subscribe(threadMode = ThreadMode.ASYNC)
	public void onMessage(TxHistoryEvent<?> e) {
		switch (e.type) {
		case START:
			wuli.start();
			refresh_lock = false;
			table_model.clearData();
			break;
		case FINISH:
			wuli.stop();
			refresh_lock = true;
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
			break;
		default:
			break;

		}
	}

}
