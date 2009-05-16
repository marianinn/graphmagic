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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Dzmitry Lazerka www.dlazerka.name
 */
public class UnloadPluginActionListener implements ActionListener {
	private final PluginsTable pluginsTable;
	private int rowIndex;

	public UnloadPluginActionListener(PluginsTable pluginsTable) {
		this.pluginsTable = pluginsTable;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		pluginsTable.removePluginAt(rowIndex);
	}

	public void setRowIndex(int rowIndex) {
		this.rowIndex = rowIndex;
	}
}
