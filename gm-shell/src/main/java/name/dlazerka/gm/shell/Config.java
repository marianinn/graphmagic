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

package name.dlazerka.gm.shell;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

/**
 * @author Dzmitry Lazerka www.dlazerka.name
 */
public class Config {
	private static final Logger logger = LoggerFactory.getLogger(Config.class);

	private static final URL CONFIG_FILEPATH;

	static {
		String path = "graphmagic.properties";
		CONFIG_FILEPATH = Config.class.getResource(path);
		if (CONFIG_FILEPATH == null) {
			throw new IllegalStateException("Path: " + path + " was not found");
		}
	}

	private static final String CONFIG_PRODUCTION_KEY = "production";
	private static final String CONFIG_DEFAULT_PLUGINS_DIR = "plugin.default.dir";
	private static final String CONFIG_PLUGIN_MAIN_CLASS = "plugin.manifest.attribute.key.for.main.class.name";
	private static final String CONFIG_DEFAULT_PRODUCTION_VALUE = "false";
	private static Properties configProperties = new Properties();

	public boolean isProduction() {
		String str = configProperties.getProperty(CONFIG_PRODUCTION_KEY, CONFIG_DEFAULT_PRODUCTION_VALUE);
		return Boolean.valueOf(str);
	}

	public String getPluginManifestAttributeKeyForMainClassName() {
		return configProperties.getProperty(CONFIG_PLUGIN_MAIN_CLASS);
	}

	public File getDefaultPluginsDir() {
		String defaultPluginsDirPath = configProperties.getProperty(CONFIG_DEFAULT_PLUGINS_DIR);
		File dir = new File(defaultPluginsDirPath);

		if (!dir.exists()) {
			String userDir = System.getProperty("user.dir");
			logger.warn(
					"{} does not exist, falling to current user.dir = {}",
					new Object[]{dir.getAbsolutePath(), userDir}
			);
			dir = new File(userDir);
		} else if (!dir.isDirectory()) {
			String userDir = System.getProperty("user.dir");
			logger.warn(
					"{} is not a directory, falling to current user.dir = {}",
					new Object[]{dir.getAbsolutePath(), userDir}
			);
			dir = new File(userDir);
		}

		if (!dir.canRead()) {
			logger.error(
					"{} is not readable",
					new Object[]{dir.getAbsolutePath()}
			);
//			ErrorDialog.showError(new IllegalStateException(""));
		}

		return dir;
	}

	public void load() throws IOException {
		try {
			configProperties.load(CONFIG_FILEPATH.openStream());
		}
		catch (IOException e) {
			logger.error("Unable to load config at {}", CONFIG_FILEPATH);
			throw e;
		}
	}
}
