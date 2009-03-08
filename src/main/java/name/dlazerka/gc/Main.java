package name.dlazerka.gc;

import name.dlazerka.gc.ui.UI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * @author Dzmitry Lazerka www.dlazerka.name
 */
public class Main {
	private static final Logger logger = LoggerFactory.getLogger(Main.class);

	private static final String MESSAGES_FILENAME = "messages";
	private static final String CONFIG_FILENAME = "graphmagic.properties";
	private static final String CONFIG_PRODUCTION_KEY = "production";
	private static final String CONFIG_DEFAULT_PRODUCTION_VALUE = "false";

	private static ResourceBundle resourceBundle = ResourceBundle.getBundle(MESSAGES_FILENAME);
	//	private static ResourceBundle configBundle = ResourceBundle.getBundle("messages");
	private static Properties configProperties = new Properties();

	static {
		try {
			configProperties.load(new FileInputStream(CONFIG_FILENAME));
		}
		catch (IOException e) {
			logger.error("Unable to load config at {}", CONFIG_FILENAME);
		}
	}

	public static String getString(String key) {
		return resourceBundle.getString(key);
	}

	public static boolean isProduction() {
		String str = configProperties.getProperty(CONFIG_PRODUCTION_KEY, CONFIG_DEFAULT_PRODUCTION_VALUE);
		return Boolean.valueOf(str);
	}

	public static void main(String[] args) {
		Locale.setDefault(Locale.ENGLISH);

		UI.show();
	}
}
