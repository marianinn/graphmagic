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

import name.dlazerka.gm.GraphMagicPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import java.awt.*;

/**
 * @author Dzmitry Lazerka www.dlazerka.name
 */
public class PluginsTable extends JTable {
	private static final Logger logger = LoggerFactory.getLogger(PluginsTable.class);
	private final ContextMenu contextMenu = new ContextMenu();

	public PluginsTable() {
		super(new PluginsTableModel());
		TableColumn column0 = getColumnModel().getColumn(0);
		column0.setCellRenderer(new PluginCellRenderer());

		setComponentPopupMenu(contextMenu);
	}

	@Override
	public PluginsTableModel getModel() {
		return (PluginsTableModel) super.getModel();
	}

	@Override
	public void setModel(TableModel dataModel) {
		if (!(dataModel instanceof PluginsTableModel)) {
			throw new IllegalArgumentException("Model must be of type " + PluginsTableModel.class.getName());
		}

		super.setModel(dataModel);
	}

	public void addPlugin(GraphMagicPlugin plugin) {
		getModel().addPlugin(plugin);
	}

	private class PluginCellRenderer extends DefaultTableCellRenderer {
		@Override
		protected void setValue(Object value) {
			if (!(value instanceof GraphMagicPlugin)) {
				throw new IllegalArgumentException("Cell value must be of type " + GraphMagicPlugin.class.getName());
			}
			GraphMagicPlugin plugin = (GraphMagicPlugin) value;
			String text = plugin.getName();
			super.setValue(text);
		}
	}

	private class ContextMenu extends JPopupMenu {
		private final JMenuItem addPluginMenuItem;

		private ContextMenu() {
			addPluginMenuItem = new JMenuItem(Main.getString("add.plugin"));
			addPluginMenuItem.addActionListener(new AddPluginActionListener(PluginsTable.this));
		}

		void setPlugin(GraphMagicPlugin plugin) {
			removeAll();

			if (plugin != null) {
				setLabel(Main.getString("plugin.actions"));
				for (Action action : plugin.getActions()) {
					add(action);
				}
			}
			else {
				setLabel(null);
				add(addPluginMenuItem);
			}
		}

		@Override
		public void show(Component invoker, int x, int y) {
			Point point = new Point(x, y);

			int rowViewIndex = rowAtPoint(point);
			int columnViewIndex = columnAtPoint(point);
			int rowModelIndex = convertRowIndexToModel(rowViewIndex);
			int columnModelIndex = convertColumnIndexToModel(columnViewIndex);

			GraphMagicPlugin plugin = null;
			if (rowModelIndex >= 0 && columnModelIndex >= 0) {
				plugin = getModel().getValueAt(rowModelIndex, columnModelIndex);
			}
			PluginsTable.this.contextMenu.setPlugin(plugin);

			super.show(invoker, x, y);
		}
	}
}
