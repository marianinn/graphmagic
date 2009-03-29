/*
 * GraphMagic, package for scientists working in graph theory.
 * Copyright (C) 2009 Dzmitry Lazerka dlazerka@dlazerka.name
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2 as
 * published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Author: Dzmitry Lazerka dlazerka@dlazerka.name
 */

package name.dlazerka.gm;

import name.dlazerka.gm.ui.UI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * @author Dzmitry Lazerka www.dlazerka.name
 */
public class Main {
	private static final Logger logger = LoggerFactory.getLogger(Main.class);

	private static final String MESSAGES_FILENAME = "messages";
	private static final URL CONFIG_FILEPATH = Main.class.getResource("/graphmagic.properties");
	private static final String CONFIG_PRODUCTION_KEY = "production";
	private static final String CONFIG_DEFAULT_PRODUCTION_VALUE = "false";

	private static ResourceBundle resourceBundle = ResourceBundle.getBundle(MESSAGES_FILENAME);
	
	private static Properties configProperties = new Properties();

	public static String getString(String key) {
		return resourceBundle.getString(key);
	}

	public static boolean isProduction() {
		String str = configProperties.getProperty(CONFIG_PRODUCTION_KEY, CONFIG_DEFAULT_PRODUCTION_VALUE);
		return Boolean.valueOf(str);
	}

	public static void main(String[] args) throws Exception {
		try {
			configProperties.load(CONFIG_FILEPATH.openStream());
		}
		catch (IOException e) {
			logger.error("Unable to load config at {}", CONFIG_FILEPATH);
			throw e;
		}
		Locale.setDefault(Locale.ENGLISH);

		UI.show();
	}
}