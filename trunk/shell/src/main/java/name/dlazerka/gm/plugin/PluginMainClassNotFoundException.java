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

package name.dlazerka.gm.plugin;

/**
 * @author Dzmitry Lazerka www.dlazerka.name
 */
public class PluginMainClassNotFoundException extends PluginLoadingException {
	public PluginMainClassNotFoundException() {
	}

	public PluginMainClassNotFoundException(String message) {
		super(message);
	}

	public PluginMainClassNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public PluginMainClassNotFoundException(Throwable cause) {
		super(cause);
	}
}
