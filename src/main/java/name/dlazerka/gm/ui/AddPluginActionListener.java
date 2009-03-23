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

import name.dlazerka.gm.Main;
import name.dlazerka.gm.plugin.PluginLoader;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * @author Dzmitry Lazerka www.dlazerka.name
 */
public class AddPluginActionListener extends JFileChooser implements ActionListener {
	private final Component parent;
	public AddPluginActionListener(Component parent) {
		super(System.getProperty("user.dir"));
		this.parent = parent;

		setDialogTitle(Main.getString("add.plugin"));
		setFileSelectionMode(JFileChooser.FILES_ONLY);
		setFileFilter(new FileNameExtensionFilter(Main.getString("jar.file"), "jar"));
	}

	public void actionPerformed(ActionEvent e) {
		int ret = showDialog(parent, Main.getString("add"));

		if (ret == JFileChooser.APPROVE_OPTION) {
			File file = getSelectedFile();
			PluginLoader.load(file);
		}
	}
}
