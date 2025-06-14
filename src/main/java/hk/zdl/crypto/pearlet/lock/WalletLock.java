package hk.zdl.crypto.pearlet.lock;

import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.Frame;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.time.Duration;
import java.util.Arrays;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import org.greenrobot.eventbus.EventBus;

import hk.zdl.crypto.pearlet.component.event.WalletLockEvent;
import hk.zdl.crypto.pearlet.component.event.WalletTimerEvent;
import hk.zdl.crypto.pearlet.persistence.MyDb;
import hk.zdl.crypto.pearlet.ui.UIUtil;
import hk.zdl.crypto.pearlet.util.Util;
import java.util.ResourceBundle; // 新增导入

public class WalletLock {
    private static final ResourceBundle rsc_bdl = Util.getResourceBundle(); // 已存在的资源绑定

	public static final String AUTO_LOCK_MIN = "AUTO_LOCK_MIN";
	public static final int MIN_PW_LEN = 8;
	private static Timer timer = new Timer();
	private static long last_unlock_time = -1;
	private static long target_lock_time = -1;
	private static char[] tmp_pw = null;
	private static Frame frame = null;

	public static void setFrame(Frame frame) {
		WalletLock.frame = frame;
	}

	public static boolean change_password() throws Exception {
		var pw_field = new JPasswordField[] { new JPasswordField(), new JPasswordField(), new JPasswordField() };
		if (LockImpl.hasPassword()) {
			// 替换旧密码提示为国际化资源
			if (UIUtil.show_password_dialog(rsc_bdl.getString("WALLET_LOCK.OLD_PASSWORD_PROMPT"), frame, pw_field[0])) {
				if (!LockImpl.validete_password(pw_field[0].getPassword())) {
					// 替换硬编码字符串为国际化资源
					JOptionPane.showMessageDialog(frame, rsc_bdl.getString("WALLET_LOCK.WRONG_PASSWORD"), null, JOptionPane.ERROR_MESSAGE);
					return false;
				}
			} else {
				return false;
			}
		} else {
			pw_field[0] = null;
		}
		// 替换新密码提示为国际化资源
		if (UIUtil.show_password_dialog(rsc_bdl.getString("WALLET_LOCK.NEW_PASSWORD_PROMPT"), frame, pw_field[1])) {
			if (pw_field[1].getPassword().length < MIN_PW_LEN) {
				// 替换带参数的提示为国际化资源
				JOptionPane.showMessageDialog(frame, String.format(rsc_bdl.getString("WALLET_LOCK.PASSWORD_TOO_SHORT"), MIN_PW_LEN), null, JOptionPane.ERROR_MESSAGE);
				return false;
			} else if (UIUtil.show_password_dialog("Re-type new password:", frame, pw_field[2])) {
				if (!Arrays.equals(pw_field[1].getPassword(), pw_field[2].getPassword())) {
					// 替换硬编码字符串为国际化资源
					JOptionPane.showMessageDialog(frame, rsc_bdl.getString("WALLET_LOCK.PASSWORD_MISMATCH"), null, JOptionPane.ERROR_MESSAGE);
					return false;
				} else {
					var d = new JDialog(frame);
					SwingUtilities.invokeLater(() -> {
						var bar = new JProgressBar();
						// 替换进度提示为国际化资源
						bar.setString(rsc_bdl.getString("WALLET_LOCK.PROGRESS_MESSAGE"));
						bar.setPreferredSize(new Dimension(500, 50));
						bar.setIndeterminate(true);
						d.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
						d.setModalityType(ModalityType.APPLICATION_MODAL);
						d.getContentPane().add(bar);
						d.setResizable(false);
						d.pack();
						d.setLocationRelativeTo(null);
						d.setVisible(true);
					});
					try {
						return LockImpl.change_password(pw_field[0] == null ? null : pw_field[0].getPassword(), pw_field[1].getPassword());
					} finally {
						d.dispose();
					}
				}
			}
		}
		return false;
	}

	public static synchronized Optional<Boolean> unlock() {
		if (!hasPassword()) {
			return Optional.of(true);
		}
		var pw_field = new JPasswordField();
		// 替换解锁时的密码输入提示为国际化资源
		if (UIUtil.show_password_dialog(rsc_bdl.getString("WALLET_LOCK.ENTER_PASSWORD_PROMPT"), frame, pw_field)) {
			if (LockImpl.validete_password(pw_field.getPassword())) {
				var i = Util.getUserSettings().getInt(WalletLock.AUTO_LOCK_MIN, -1);
				if (i > 0) {
					last_unlock_time = System.currentTimeMillis();
					target_lock_time = last_unlock_time + Duration.ofMinutes(i).toMillis();
				} else {
					last_unlock_time = -1;
					target_lock_time = -1;
				}
				tmp_pw = pw_field.getPassword();
				timer.cancel();
				timer = new Timer();
				timer.scheduleAtFixedRate(getTimerTask(), 0, i <= 1 ? 500 : 2000);
				return Optional.of(true);
			} else {
				return Optional.of(false);
			}
		}
		return Optional.empty();
	}

	public static final boolean hasPassword() {
		return LockImpl.hasPassword();
	}

	public static boolean lock() {
		tmp_pw = null;
		timer.cancel();
		EventBus.getDefault().post(new WalletTimerEvent(0, 100));
		return hasPassword();
	}

	public static boolean isLocked() {
		return LockImpl.hasPassword() && tmp_pw == null;
	}

	public static byte[] encrypt_private_key(byte[] bArr) throws Exception {
		return LockImpl.aes_encrypt(tmp_pw, bArr);
	}

	public static byte[] decrypt_private_key(int network_id, int account_id) throws Exception {
		var bArr = MyDb.get_encpvk(network_id, account_id);
		return LockImpl.aes_decrypt(tmp_pw, bArr);
	}

	public static String decrypt_passphrase(int network_id, int account_id) throws Exception {
		var bArr = MyDb.get_encpse(network_id, account_id);
		if (bArr == null) {
			return null;
		}
		bArr = LockImpl.aes_decrypt(tmp_pw, bArr);
		return Charset.defaultCharset().decode(ByteBuffer.wrap(bArr)).toString().trim();
	}

	private static final TimerTask getTimerTask() {
		return new TimerTask() {

			@Override
			public void run() {
				if (target_lock_time < 0) {
					return;
				} else if (System.currentTimeMillis() < target_lock_time) {
					var a = target_lock_time - last_unlock_time;
					var b = target_lock_time - System.currentTimeMillis();
					var c = 1000F * b / a;
					EventBus.getDefault().post(new WalletTimerEvent((int) c, 1000));
				} else {
					EventBus.getDefault().post(new WalletTimerEvent(0, 100));
					EventBus.getDefault().post(new WalletLockEvent(WalletLockEvent.Type.LOCK));
					timer.cancel();
					// 替换硬编码字符串为国际化资源
					UIUtil.displayMessage(rsc_bdl.getString("WALLET_LOCK.LOCKED_MESSAGE"), "");
				}
			}
		};
	}

}
