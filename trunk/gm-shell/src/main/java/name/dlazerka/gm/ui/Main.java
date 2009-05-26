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

package name.dlazerka.gm.ui;

import name.dlazerka.gm.GraphsContainer;
import name.dlazerka.gm.pluginloader.PluginLoader;
import name.dlazerka.gm.pluginloader.PluginLoadingException;
import name.dlazerka.gm.pluginloader.PluginWrapper;
import name.dlazerka.gm.shell.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.File;
import java.io.FilenameFilter;
import java.util.Locale;

/**
 * @author Dzmitry Lazerka www.dlazerka.name
 */
public class Main {
	private static final Logger logger = LoggerFactory.getLogger(Main.class);

	private static final Config config = new Config();


	private static Locale currentLocale = Locale.ENGLISH;
	private static GraphsContainer graphMagicAPI = new GraphsContainer();
	private static PluginLoader pluginLoader = new PluginLoader(graphMagicAPI);

	public static Config getConfig() {
		return config;
	}

	public static void main(String[] args) throws Exception {
		config.load();

		// set locale
		Locale.setDefault(getCurrentLocale());

		// catch unhandled exceptions
		EventQueue queue = Toolkit.getDefaultToolkit().getSystemEventQueue();
		queue.push(new ErrorCatchingEventQueue());

		// show user interface
		UI.show();

		loadDefaultPlugins();
	}

	private static void loadDefaultPlugins() {
		Config config = Main.getConfig();
		File dir = config.getDefaultPluginsDir();
        FilenameFilter jarFilter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                String s = name.toLowerCase();
                return s.endsWith(".jar");
            }
        };
        for (File file : dir.listFiles(jarFilter)) {
			try {
				PluginWrapper pluginWrapper = pluginLoader.load(file);
				UI.registerPlugin(pluginWrapper);
			}
			catch (PluginLoadingException e) {
				ErrorDialog.showError(e, UI.getMainFrame());
			}
		}
	}

	public static Locale getCurrentLocale() {
		return currentLocale;
	}

	public static GraphsContainer getGraphMagicAPI() {
		return graphMagicAPI;
	}

	public static PluginLoader getPluginLoader() {
		return pluginLoader;
	}
}