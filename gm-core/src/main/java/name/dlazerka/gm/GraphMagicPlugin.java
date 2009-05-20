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

import javax.swing.*;
import java.util.List;
import java.util.Locale;

/**
 * Any plugin must implement this interface.
 *
 * @author Dzmitry Lazerka www.dlazerka.name
 * @see AbstractPlugin for abstract implementation
 */
public interface GraphMagicPlugin {

	/**
	 * Called when plugin has just added to the application.
	 *
	 * @param api	API of the core
	 * @param locale user selected locale
	 */
	void init(GraphMagicAPI api, Locale locale);

	/**
	 * Plugin must return it's available actions.
	 *
	 * @return list of available actions
	 */
	List<Action> getActions();

	/**
	 * @return name of this plugin in the list of plugins.
	 */
	String getName();
}
