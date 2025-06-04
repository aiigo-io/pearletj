package hk.zdl.crypto.pearlet.component.dashboard.ether;

import java.awt.Color;
import java.awt.Component;
import java.math.BigDecimal;

import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

import org.json.JSONObject;
import org.web3j.utils.Convert;

import com.jthemedetecor.OsThemeDetector;

import hk.zdl.crypto.pearlet.ds.CryptoNetwork;
import hk.zdl.crypto.pearlet.ds.CryptoNetwork.Type;

@SuppressWarnings("serial")
public class EtherValueCellRenderer extends DefaultTableCellRenderer {

	private static final OsThemeDetector otd = OsThemeDetector.getDetector();
	private static final Color my_cyan = new Color(0, 175, 175), my_lime = new Color(175, 255, 175);
	private static final CryptoNetwork network = new CryptoNetwork();
	private final String address;
	static {
		network.setType(Type.WEB3J);
	}

	public EtherValueCellRenderer(String address) {
		this.address = address;
		setHorizontalAlignment(SwingConstants.RIGHT);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		boolean isDark = otd.isDark();
		if (!isSelected) {
			var tx = (JSONObject) value;
			if (tx.optInt("type") == 0) {
				if (tx.getJSONObject("from").getString("hash").equalsIgnoreCase(address)) {
					setBackground(isDark ? darker(Color.pink) : Color.pink);
				} else {
					setBackground(isDark ? darker(my_lime) : my_lime);
				}
			} else {
				setBackground(isDark ? darker(my_cyan) : my_cyan);
			}

		}
		return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	}

	@Override
	protected void setValue(Object value) {
		var tx = (JSONObject) value;
		if (tx.has("text")) {
			setText(tx.getString("text"));
		} else {
			var val = Convert.fromWei(new BigDecimal(tx.getString("value")), Convert.Unit.ETHER);
			setText(val.stripTrailingZeros().toPlainString());
		}
	}

	private static final Color darker(Color c) {
		float factor = 0.4f;
		return new Color(Math.max((int) (c.getRed() * factor), 0), Math.max((int) (c.getGreen() * factor), 0), Math.max((int) (c.getBlue() * factor), 0), c.getAlpha());
	}

}
