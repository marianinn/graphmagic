/*
 * GraphMagic, package for scientists working in graph theory.
 * Copyright (C) 2010 Dzmitry Lazerka dlazerka@dlazerka.name
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

import name.dlazerka.gm.Graph;
import name.dlazerka.gm.Vertex;
import name.dlazerka.gm.Edge;
import name.dlazerka.gm.shell.ResourceBundle;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.*;
import java.text.MessageFormat;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.util.regex.Pattern;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

/**
 * @author Dzmitry Lazerka www.dlazerka.name
 */
public class SaveGraphActionListener extends JFileChooser implements ActionListener {
	private static final Logger logger = LoggerFactory.getLogger(SaveGraphActionListener.class);
	private final Component parent;
	private final Graph graph;

	public SaveGraphActionListener(Component parent, Graph graph) {
		this.parent = parent;
		this.graph = graph;
		setDialogTitle(ResourceBundle.getString("save.graph"));
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
		if (showDialog(parent, ResourceBundle.getString("save.graph")) != JFileChooser.APPROVE_OPTION) {
			return;
		}
		try {
			File file = getSelectedFile();

			file = maybeAddExtension(file);

			if (file.exists()) {
				String message = ResourceBundle.getString("file.exists.overwrite");
				message = MessageFormat.format(message, file.getCanonicalPath());
				if (JOptionPane.showConfirmDialog(
					parent, message
				) != JOptionPane.YES_OPTION) {
					return;
				}
			}

			saveGraphML(file);
		}
		catch (IOException e1) {
			ErrorDialog.showError(e1, parent);
		}
	}

	private File maybeAddExtension(File file) throws IOException {
		String filePath = file.getCanonicalPath();

		String ext = ResourceBundle.getString("graphml.ext");
		String regex = ".*" + Pattern.quote(ext) + "$";
		if (!filePath.matches(regex)) {
			filePath += "." + ext;
			file = new File(filePath);
		}
		return file;
	}

	private void saveGraphML(File file) throws IOException {
		logger.info("Saving graph to {}", file.getCanonicalPath());

		String headerFormat = ResourceBundle.getString("graphml.header");
		String nodeFormat = ResourceBundle.getString("graphml.node");
		String edgeFormat = ResourceBundle.getString("graphml.edge");
		String footer = ResourceBundle.getString("graphml.footer");

		String directed = graph.isDirected() ? "directed" : "undirected";
		String header = MessageFormat.format(headerFormat, directed);

		FileWriter fileWriter;
		fileWriter = new FileWriter(file);
		fileWriter.write(header);
		for (Vertex vertex : graph.getVertexSet()) {
			fileWriter.write(MessageFormat.format(nodeFormat, vertex.getId()));
		}
		logger.debug("Vertices written. Writing edges...");
		int edgeId = 1;
		for (Edge edge : graph.getEdgeSet()) {
			String sourceId = edge.getTail().getId();
			String targetId = edge.getHead().getId();
			fileWriter.write(MessageFormat.format(edgeFormat, edgeId, sourceId, targetId));
			edgeId++;
		}
		fileWriter.write(footer);
		fileWriter.close();

		logger.info("Saved graph to {}", file.getCanonicalPath());
	}
}
