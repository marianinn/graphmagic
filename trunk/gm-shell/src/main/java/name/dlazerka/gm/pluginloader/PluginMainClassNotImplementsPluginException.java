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

import name.dlazerka.gm.ResourceBundle;

/**
 * @author Dzmitry Lazerka www.dlazerka.name
 */
public class PluginMainClassNotImplementsPluginException extends PluginLoadingException {
	private final Class<?> mainClass;

	public PluginMainClassNotImplementsPluginException(Class<?> mainClass) {
		super(
			"Plugin class " + mainClass.getName() + " does not implement the name.dlazerka.gm.GraphMagicPlugin interface"
		);
		this.mainClass = mainClass;
	}

	@Override
	public String getLocalizedMessage() {
		String s = ResourceBundle.getString("plugin.class.not.implements.graphmagicplugin", mainClass.getName());
		return super.getLocalizedMessage() + ": " + s;
	}
}
