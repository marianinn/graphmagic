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

import java.io.File;

/**
 * @author Dzmitry Lazerka www.dlazerka.name
 */
public class PluginLoadingException extends Exception {
	private File file;
	private String filePath;

	public PluginLoadingException(String message) {
		super("Error loading plugin: " + message);
	}

	public PluginLoadingException(String message, Throwable cause) {
		super("Error loading plugin: " + message, cause);
	}

	public PluginLoadingException(String message, File file) {
		super("Error loading plugin from file " + file.getAbsolutePath() + ": " + message);
		this.file = file;
	}

	public PluginLoadingException(String message, File file, Throwable cause) {
		super("Error loading plugin from file " + file.getAbsolutePath() + ": " + message, cause);
		this.file = file;
	}

	public PluginLoadingException(String message, String filePath) {
		super("Error loading plugin from path " + filePath + ": " + message);
		this.filePath = filePath;
	}

	public PluginLoadingException(String message, String filePath, Throwable cause) {
		super("Error loading plugin from path " + filePath + ": " + message, cause);
		this.filePath = filePath;
	}

	@Override
	public String getLocalizedMessage() {
		if (file != null) {
			return ResourceBundle.getString("error.loading.plugin.from.file", file.getAbsolutePath());
		}
		else if (filePath != null) {
			return ResourceBundle.getString("error.loading.plugin.from.path", filePath);
		}
		else {
			return ResourceBundle.getString("error.loading.plugin");
		}
	}

	/**
	 * Differs from {@link super#toString()} in the way that message is obtained
	 * from {@link #getMessage()} instead of {@link #getLocalizedMessage()}.
	 * Others are the same:
	 * <p/>
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		String s = getClass().getName();
		String message = getMessage();
		return (message != null) ? (s + ": " + message) : s;
	}

	public File getFile() {
		return file;

	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
}
