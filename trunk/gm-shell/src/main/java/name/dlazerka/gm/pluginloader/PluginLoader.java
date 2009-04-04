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

package name.dlazerka.gm.pluginloader;

import name.dlazerka.gm.GraphMagicAPI;
import name.dlazerka.gm.GraphMagicPlugin;
import name.dlazerka.gm.ui.Config;
import name.dlazerka.gm.ui.ErrorDialog;
import name.dlazerka.gm.ui.Main;
import name.dlazerka.gm.ui.UI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.JarFilter;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 * @author Dzmitry Lazerka www.dlazerka.name
 */
public class PluginLoader {
	private static final Logger logger = LoggerFactory.getLogger(PluginLoader.class);

	private final GraphMagicAPI graphMagicAPI;

	public PluginLoader(GraphMagicAPI graphMagicAPI) {
		this.graphMagicAPI = graphMagicAPI;
	}

	public GraphMagicPlugin load(File file) throws PluginLoadingException {
		if (!file.isFile()) {
			throw new PluginNotFileException(file.getName());
		}

		JarFile jarFile;
		Manifest manifest;
		try {
			jarFile = new JarFile(file);
			manifest = jarFile.getManifest();
		}
		catch (IOException e) {
			throw new PluginManifestLoadingException(file, e);
		}

		if (manifest == null) {
			throw new PluginManifestAbsentException(file);
		}

		Config config = Main.getConfig();
		String attributeName = config.getPluginManifestAttributeKeyForMainClassName();
		
		Attributes mainAttributes = manifest.getMainAttributes();
		String pluginClassName = mainAttributes.getValue(attributeName);

		if (pluginClassName == null) {
			throw new PluginMainClassNotSpecifiedException(attributeName, file);
		}

		URI uri = file.toURI();
		URL url;
		try {
			url = uri.toURL();
		}
		catch (MalformedURLException e) {
			throw new PluginLoadingException("Unknown error", file, e);
		}

		URLClassLoader loader = URLClassLoader.newInstance(
			new URL[]{url},
			PluginLoader.class.getClassLoader()
		);

		Class<?> mainClass;
		try {
			mainClass = loader.loadClass(pluginClassName);
		}
		catch (ClassNotFoundException e) {
			throw new PluginClassNotFoundException(pluginClassName, file, e);
		}

		try {
			return load(mainClass);
		}
		catch (PluginLoadingException e) {
			e.setFile(file);
			throw e;
		}
	}

	public GraphMagicPlugin load(Class<?> mainClass) throws PluginLoadingException {
		if (!GraphMagicPlugin.class.isAssignableFrom(mainClass)) {
			throw new PluginMainClassNotImplementsPluginException(mainClass);
		}
		
		@SuppressWarnings("unchecked")
		Class<GraphMagicPlugin> pluginMainClass = (Class<GraphMagicPlugin>) mainClass;

		Constructor<GraphMagicPlugin> constructor;
		try {
			constructor = pluginMainClass.getConstructor();
		}
		catch (NoSuchMethodException e) {
			throw new PluginMainClassNoEmptyConstructorException(pluginMainClass);
		}

		GraphMagicPlugin pluginInstance;
		try {
			pluginInstance = constructor.newInstance();
		}
		catch (InstantiationException e) {
			throw new PluginClassInstatiationException(pluginMainClass, e);
		}
		catch (IllegalAccessException e) {
			throw new PluginClassInstatiationException(pluginMainClass, e);
		}
		catch (InvocationTargetException e) {
			throw new PluginClassInstatiationException(pluginMainClass, e);
		}

		init(pluginInstance);

		return pluginInstance;
	}

	public GraphMagicPlugin init(GraphMagicPlugin pluginInstance) {
		pluginInstance.setGraphMagicAPI(graphMagicAPI);
		pluginInstance.setLocale(Main.getCurrentLocale());

		return pluginInstance;
	}

	public void loadDefaultPlugins() {
		Config config = Main.getConfig();
		File dir = config.getDefaultPluginsDir();
		for (File file : dir.listFiles(new JarFilter())) {
			try {
				load(file);
			}
			catch (PluginLoadingException e) {
				ErrorDialog.showError(e, UI.getMainFrame());
			}
		}
	}
}
