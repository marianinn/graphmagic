/*
 * GraphMagic, package for scientists working in graph theory.
 * Copyright (C) 2010 Dzmitry Lazerka www.dlazerka.name
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
 * Author: Dzmitry Lazerka www.dlazerka.name
 */

package name.dlazerka.gm.shell.store;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import name.dlazerka.gm.Graph;
import name.dlazerka.gm.basic.BasicGraph;
import name.dlazerka.gm.basic.MergeException;
import name.dlazerka.gm.shell.ResourceBundle;
import name.dlazerka.gm.ui.ErrorDialog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Dzmitry Lazerka www.dlazerka.name
 */
public class LoadGraphActionListener extends JFileChooser implements ActionListener {
	private static final Logger logger = LoggerFactory.getLogger(LoadGraphActionListener.class);
	private final Component parent;
	private final Graph graph;

	public LoadGraphActionListener(Component parent, Graph graph) {
		this.parent = parent;
		this.graph = graph;
		setDialogTitle(ResourceBundle.getString("load.graph"));
		setFileSelectionMode(JFileChooser.FILES_ONLY);
		setFileFilter(
			new FileNameExtensionFilter(
				ResourceBundle.getString("graphml.files"),
				ResourceBundle.getString("graphml.ext")
			)
		);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		int ret = showOpenDialog(parent);

		if (ret == JFileChooser.APPROVE_OPTION) {
			File file = getSelectedFile();
			try {
				BasicGraph graph = GraphmlStorer.load(file);
				try {
					this.graph.mergeFrom(graph);
				}
				catch (MergeException e1) {
					throw new GraphLoadingException(e1);
				}
			}
			catch (GraphLoadingException e1) {
				ErrorDialog.showError(e1, parent);
			}
		}
	}
}
