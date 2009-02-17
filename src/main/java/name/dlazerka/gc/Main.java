package name.dlazerka.gc;

import name.dlazerka.gc.ui.UI;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author Dzmitry Lazerka
 */
public class Main {
	private static ResourceBundle resourceBundle; 

	public static String getString(String key) {
		return resourceBundle.getString(key);
	}

	public static void main(String[] args) {
		Locale.setDefault(Locale.ENGLISH);

		resourceBundle = ResourceBundle.getBundle("messages");
		UI.show();
	}
}
