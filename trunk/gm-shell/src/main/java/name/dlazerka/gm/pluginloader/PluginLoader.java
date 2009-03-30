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

import name.dlazerka.gm.GraphMagicPlugin;
import name.dlazerka.gm.GraphMagicAPI;
import name.dlazerka.gm.ui.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * @author Dzmitry Lazerka www.dlazerka.name
 */
public class PluginLoader {
	private static final Logger logger = LoggerFactory.getLogger(PluginLoader.class);

	private final GraphMagicAPI graphMagicAPI;

	public PluginLoader(GraphMagicAPI graphMagicAPI) {
		this.graphMagicAPI = graphMagicAPI;
	}

	public void load(File file) throws PluginLoadingException {
		URLClassLoader loader;
		try {
			URI uri = file.toURI();
			URL url = uri.toURL();
			loader = URLClassLoader.newInstance(
				new URL[]{url},
				PluginLoader.class.getClassLoader()
			);
		}
		catch (MalformedURLException e) {
			throw new PluginLoadingException(e);
		}

		Class<?> mainClass;
		try {
			mainClass = loader.loadClass("Main");
		}
		catch (ClassNotFoundException e) {
			throw new PluginMainClassNotFoundException(e);
		}

		load(mainClass);
	}

	public void load(Class<?> mainClass) throws PluginLoadingException {
		if (!GraphMagicPlugin.class.isAssignableFrom(mainClass)) {
			throw new PluginMainClassNotExtendPluginException(mainClass);
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
			throw new PluginLoadingException(e);
		}
		catch (IllegalAccessException e) {
			throw new PluginLoadingException(e);
		}
		catch (InvocationTargetException e) {
			throw new PluginLoadingException(e);
		}

		load(pluginInstance);
	}

	public void load(GraphMagicPlugin pluginInstance) {
		pluginInstance.setGraphMagicAPI(graphMagicAPI);
		pluginInstance.setLocale(Main.getCurrentLocale());
	}
}
