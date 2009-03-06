package name.dlazerka.gc;

import name.dlazerka.gc.ui.UI;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author Dzmitry Lazerka
 */
public class Main {
	private static ResourceBundle resourceBundle = ResourceBundle.getBundle("messages");

	public static String getString(String key) {
		return resourceBundle.getString(key);
	}

	public static void main(String[] args) {
		Locale.setDefault(Locale.ENGLISH);

		UI.show();
	}
}
