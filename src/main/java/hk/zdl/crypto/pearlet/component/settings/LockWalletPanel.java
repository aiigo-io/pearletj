package hk.zdl.crypto.pearlet.component.settings;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;
import java.util.stream.Stream;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import hk.zdl.crypto.pearlet.component.event.WalletLockEvent;
import hk.zdl.crypto.pearlet.lock.WalletLock;
import hk.zdl.crypto.pearlet.ui.UIUtil;
import hk.zdl.crypto.pearlet.util.Util;

public class LockWalletPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 9007587563161970151L;
	private static final ResourceBundle rsc_bdl = Util.getResourceBundle();
	private final Icon locked_icon = UIUtil.getStretchIcon("icon/lock-closed.svg", 32, 32);
	private final Icon unlocked_icon = UIUtil.getStretchIcon("icon/lock-open.svg", 32, 32);
	private final JToggleButton t_btn = new JToggleButton();
	private final JButton chg_pwd_btn = new JButton(rsc_bdl.getString("SETTINGS.LOCK.CHANGE_PASSWORD"));
	private final JComboBox<Entry> box = new JComboBox<>(Entry.values());

	public LockWalletPanel() {
		EventBus.getDefault().register(this);
		t_btn.addActionListener(this);
		t_btn.setPreferredSize(new Dimension(150, 32));
		t_btn.setHorizontalAlignment(SwingConstants.LEFT);
		setLayout(new FlowLayout());
		var panel = new JPanel(new GridLayout(0, 1));
		var panel_1 = new JPanel(new GridBagLayout());
		panel_1.add(new JLabel(rsc_bdl.getString("SETTINGS.LOCK.NOW_STATE")),
				new GridBagConstraints(0, 0, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 2, 2));
		panel_1.add(t_btn, new GridBagConstraints(1, 0, 1, 1, 0, 0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 2, 2));
		panel_1.add(new JLabel(rsc_bdl.getString("SETTINGS.LOCK.COUNT_DOWN")),
				new GridBagConstraints(0, 1, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 2, 2));
		panel_1.add(box, new GridBagConstraints(1, 1, 1, 1, 0, 0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 2, 2));
		panel_1.add(chg_pwd_btn, new GridBagConstraints(0, 2, 2, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 2, 2));
		panel.add(panel_1);
		add(panel);
		box.addActionListener(e -> {
			var x = (Entry) box.getSelectedItem();
			var p = Util.getUserSettings();
			p.putInt(WalletLock.AUTO_LOCK_MIN, x.val);
			Util.submit(() -> {
				p.flush();
				return null;
			});
		});
		chg_pwd_btn.addActionListener(e -> {
			Util.submit(() -> {
				try {
					WalletLock.change_password();
				} catch (Exception x) {
					JOptionPane.showMessageDialog(getRootPane(), x.getMessage(), x.getClass().getSimpleName(), JOptionPane.ERROR_MESSAGE);
				}
			});
		});
		SwingUtilities.invokeLater(() -> {
			var i = Util.getUserSettings().getInt(WalletLock.AUTO_LOCK_MIN, -1);
			Stream.of(Entry.values()).filter(e -> e.val == i).findFirst().ifPresent(e -> box.setSelectedItem(e));
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (t_btn.isSelected()) {
			if (WalletLock.lock()) {
				EventBus.getDefault().post(new WalletLockEvent(WalletLockEvent.Type.LOCK));
			} else {
				t_btn.setSelected(false);
			}
		} else {
			var o = WalletLock.unlock();
			if (o.isPresent()) {
				if (o.get()) {
					EventBus.getDefault().post(new WalletLockEvent(WalletLockEvent.Type.UNLOCK));
				} else {
					JOptionPane.showMessageDialog(getRootPane(), rsc_bdl.getString("TRAY.WRONG_PW"), null, JOptionPane.ERROR_MESSAGE);
					t_btn.setSelected(true);
				}
			} else {
				t_btn.setSelected(true);
			}
		}
	}

	@Subscribe(threadMode = ThreadMode.ASYNC)
	public void onMessage(WalletLockEvent e) {
		if (e.type == WalletLockEvent.Type.LOCK) {
			t_btn.setIcon(locked_icon);
			t_btn.setText(rsc_bdl.getString("SETTINGS.LOCK.LOCKED"));
			t_btn.setSelected(true);
			chg_pwd_btn.setEnabled(false);
			box.setEnabled(false);
		} else {
			t_btn.setIcon(unlocked_icon);
			t_btn.setText(rsc_bdl.getString("SETTINGS.LOCK.UNLOCKED"));
			t_btn.setSelected(false);
			chg_pwd_btn.setEnabled(true);
			box.setEnabled(true);
		}
	}

	public enum Entry {
		ONE_MINUTE(1), TEN_MINUTE(10), QUARTER(15), HALF_HOUR(30), NEVER(-1);

		private final int val;

		private Entry(int val) {
			this.val = val;
		}

		public int getValue() {
			return val;
		}

		public String toString() {
			switch (this) {
			case HALF_HOUR:
				return rsc_bdl.getString("SETTINGS.LOCK.HALF_HOUR");
			case NEVER:
				return rsc_bdl.getString("SETTINGS.LOCK.NEVER");
			case ONE_MINUTE:
				return rsc_bdl.getString("SETTINGS.LOCK.ONE_MINUTE");
			case QUARTER:
				return rsc_bdl.getString("SETTINGS.LOCK.QUARTER");
			case TEN_MINUTE:
				return rsc_bdl.getString("SETTINGS.LOCK.TEN_MINUTE");
			}
			return "";
		}
	}

}
