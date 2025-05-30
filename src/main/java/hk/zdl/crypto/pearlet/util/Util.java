package hk.zdl.crypto.pearlet.util;

import java.awt.Desktop;
import java.awt.Desktop.Action;
import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TimeZone;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.prefs.Preferences;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.vaadin.open.Open;

import hk.zdl.crypto.pearlet.ds.CryptoNetwork;
import hk.zdl.crypto.pearlet.ui.UIUtil;
import net.harawata.appdirs.AppDirsFactory;
import signumj.entity.response.Transaction;

public class Util {

	private static final ExecutorService es = Executors.newCachedThreadPool((r) -> {
		var t = new Thread(r, "");
		t.setDaemon(true);
		return t;
	});

	public static final Prop getProp() {
		return PropKit.use("config.txt");
	}

	public static final String getDBURL() {
		var db_name = getProp().get("dbName");
		var user_dir = getUserDataDir();
		user_dir += File.separator + db_name;
		user_dir = user_dir.replace('\\', '/');
		var db_url = "jdbc:derby:directory:" + user_dir + ";create=true";
		return db_url;
	}

	public static final String getUserDataDir() {
		var prop = getProp();
		return AppDirsFactory.getInstance().getUserDataDir(prop.get("appName"), prop.get("appVersion"), prop.get("appAuthor"), false);
	}

	public static final Preferences getUserSettings() {
		return Preferences.userNodeForPackage(Util.class);
	}

	public static final String getAppVersion() {
		var text = Util.class.getPackage().getImplementationVersion();
		if (text == null) {
			text = getProp().get("appVersion");
		}
		return text;
	}

	public static final DateFormat getDateFormat() {
		if (Locale.getDefault().toString().startsWith("zh_CN")) {
			var sdf = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss", Locale.SIMPLIFIED_CHINESE);
			sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
			return sdf;
		} else {
			return DateFormat.getDateTimeInstance();
		}
	}

	public static final ResourceBundle getResourceBundle() {
		return ResourceBundle.getBundle("i18n.MessagesBundle");
	}

	public static final String getResourceAsText(String path) {
		try {
			return IOUtils.toString(Util.class.getClassLoader().getResourceAsStream(path), Charset.defaultCharset());
		} catch (IOException e) {
			return null;
		}
	}

	public static final byte[] getResourceAsByteArray(String path) {
		try {
			return IOUtils.toByteArray(Util.class.getClassLoader().getResourceAsStream(path));
		} catch (IOException e) {
			return null;
		}
	}

	public static final URL getResource(String path) {
		return Util.class.getClassLoader().getResource(path);
	}

	public static final <T> Future<T> submit(Callable<T> task) {
		return es.submit(task);
	}

	public static final Future<?> submit(Runnable task) {
		return es.submit(task);
	}

	public static Long getTime(Class<?> cl) {
		try {
			var rn = cl.getName().replace('.', '/') + ".class";
			var j = (JarURLConnection) cl.getClassLoader().getResource(rn).openConnection();
			return j.getJarFile().getEntry("META-INF/MANIFEST.MF").getTime();
		} catch (Exception e) {
			return null;
		}
	}

	public static final String get_icon_file_name(CryptoNetwork o, boolean isDark) {
		if (o.getType() == CryptoNetwork.Type.WEB3J) {
			return "ethereum-crypto-cryptocurrency-2-svgrepo-com.svg";
		} else if (is_signum_server_address(o.getUrl())) {
			return "Signum_Logomark_black.png";
		} else {
			try {
				var url = o.getUrl();
				var jarr = get_predefined_networks();
				for (var i = 0; i < jarr.length(); i++) {
					var jobj = jarr.getJSONObject(i);
					if (url.equals(jobj.getString("server url"))) {
						var s = jobj.getString("icon");
						if (isDark) {
							s = jobj.optString("icon_dark", s);
						}
						return s;
					}
				}
			} catch (Exception x) {
			}
		}
		return "blockchain-dot-com-svgrepo-com.svg";
	}

	public static final JSONArray get_predefined_networks() throws Exception {
		var in = UIUtil.class.getClassLoader().getResourceAsStream("network/predefined.json");
		var jarr = new JSONArray(new JSONTokener(in));
		in.close();
		return jarr;
	}

	public static final boolean is_signum_server_address(String str) {
		try {
			var list = IOUtils.readLines(UIUtil.class.getClassLoader().getResourceAsStream("network/signum.txt"), Charset.defaultCharset());
			if (list.contains(str)) {
				return true;
			}
		} catch (Exception e) {
		}
		return false;
	}

	public static final <E> void viewContractDetail(CryptoNetwork nw, E e) throws Exception {
		if (nw.isWeb3J()) {
			var address = ((JSONObject) e).optString("contract_address");
			browse(new URI("http://explorer.aiigo.org/api/v2/search?q=" + address));
		}
	}

	public static final <E> void viewTxDetail(CryptoNetwork nw, E e) throws Exception {
		if (nw.isWeb3J()) {
			var tx = (JSONObject) e;
			browse(new URI("http://explorer.aiigo.org/tx/" + tx.getString("hash")));
		} else if (is_signum_server_address(nw.getUrl())) {
			Transaction tx = (Transaction) e;
			String tx_id = tx.getId().toString();
			browse(new URI("https://chain.signum.network/tx/" + tx_id));
		} else {
			Transaction tx = (Transaction) e;
			String tx_id = tx.getId().toString();
			var jarr = get_predefined_networks();
			for (var i = 0; i < jarr.length(); i++) {
				var jobj = jarr.getJSONObject(i);
				if (nw.getUrl().equals(jobj.getString("server url"))) {
					browse(new URI(jobj.getString("explorer url") + tx_id));
				}
			}
		}
	}

	public static final void viewBlockDetail(CryptoNetwork nw, String block_id) throws Exception {
		if (nw.isWeb3J()) {
			browse(new URI("http://explorer.aiigo.org/block/" + block_id));
		} else if (is_signum_server_address(nw.getUrl())) {
			browse(new URI("https://chain.signum.network/block/" + block_id));
		} else {
			var jarr = get_predefined_networks();
			for (var i = 0; i < jarr.length(); i++) {
				var jobj = jarr.getJSONObject(i);
				if (nw.getUrl().equals(jobj.getString("server url"))) {
					browse(new URI(jobj.getString("explorer url") + block_id));
				}
			}
		}
	}

	public static final void viewAccountDetail(CryptoNetwork nw, String e) throws Exception {
		if (nw.isWeb3J()) {
			browse(new URI("http://explorer.aiigo.org/address/" + e));
		} else if (is_signum_server_address(nw.getUrl())) {
			browse(new URI("https://chain.signum.network/address/" + e));
		} else {
			var jarr = get_predefined_networks();
			for (var i = 0; i < jarr.length(); i++) {
				var jobj = jarr.getJSONObject(i);
				if (nw.getUrl().equals(jobj.getString("server url"))) {
					browse(new URI(jobj.getString("explorer url") + e));
				}
			}
		}
	}

	public static void browse(URI uri) throws Exception {
		if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Action.BROWSE)) {
			Desktop.getDesktop().browse(uri);
		} else {
			Open.open(uri.toString());
		}
	}
}
