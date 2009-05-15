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
import name.dlazerka.gm.PluginException;
import name.dlazerka.gm.ResourceBundle;
import name.dlazerka.gm.pluginloader.PluginWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import java.awt.*;
import java.io.File;

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

        setFont(getFont().deriveFont(Font.PLAIN, 15));

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

	public void addPlugin(PluginWrapper pluginWrapper) {
		getModel().addRow(pluginWrapper);
	}

	public void setPlugin(PluginWrapper pluginWrapper, int rowModelIndex) {
		getModel().setRow(pluginWrapper, rowModelIndex);
	}

	private class PluginCellRenderer extends DefaultTableCellRenderer {
        private PluginCellRenderer() {
            setFont(PluginsTable.this.getFont());
//            this.set
        }

        @Override
		protected void setValue(Object value) {
			if (!(value instanceof PluginWrapper)) {
				throw new IllegalArgumentException("Cell value must be of type " + GraphMagicPlugin.class.getName());
			}
			PluginWrapper pluginWrapper = (PluginWrapper) value;
			GraphMagicPlugin plugin = pluginWrapper.getPlugin();
			File file = pluginWrapper.getFile();

			String text = plugin.getName();
			setToolTipText(file.getAbsolutePath());

			super.setValue(text);
		}
	}

	private class ContextMenu extends JPopupMenu {
		private final JMenuItem addPluginMenuItem;
		private final JMenuItem reloadPluginMenuItem;
		private ReloadPluginActionListener reloadActionListener = new ReloadPluginActionListener(PluginsTable.this);

		private ContextMenu() {
			addPluginMenuItem = new JMenuItem(ResourceBundle.getString("add.plugin"));
			addPluginMenuItem.addActionListener(new AddPluginActionListener(PluginsTable.this));
			reloadPluginMenuItem = new JMenuItem(ResourceBundle.getString("reload.plugin"));
			reloadPluginMenuItem.addActionListener(reloadActionListener);
		}

		void setPlugin(PluginWrapper pluginWrapper, int rowModelIndex) {
			removeAll();

			if (pluginWrapper != null) {
				GraphMagicPlugin plugin = pluginWrapper.getPlugin();
				File file = pluginWrapper.getFile();

				setLabel(ResourceBundle.getString("plugin.actions"));

                java.util.List<Action> actionList = plugin.getActions();

                if (actionList == null) {
                    PluginException exception = new PluginException("Action list cannot be null");
                    throw exception;
                }

				for (Action action : actionList) {
					add(action);
				}

                addSeparator();

                reloadActionListener.setFile(file);
                reloadActionListener.setRowIndex(rowModelIndex);
                add(reloadPluginMenuItem);
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

			PluginWrapper plugin = null;
			if (rowModelIndex >= 0 && columnModelIndex >= 0) {
				plugin = getModel().getValueAt(rowModelIndex, columnModelIndex);
			}
			setPlugin(plugin, rowModelIndex);

			super.show(invoker, x, y);
		}
	}

}
