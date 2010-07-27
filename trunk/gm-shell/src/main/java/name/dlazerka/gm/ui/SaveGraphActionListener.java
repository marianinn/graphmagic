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

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.text.MessageFormat;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author Dzmitry Lazerka www.dlazerka.name
 */
public class SaveGraphActionListener implements ActionListener {
	private Graph graph;

	public SaveGraphActionListener(Graph graph) {
		this.graph = graph;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String headerFormat = ResourceBundle.getString("graphml.header");
		String nodeFormat = ResourceBundle.getString("graphml.node");
		String edgeFormat = ResourceBundle.getString("graphml.edge");
		String footer = ResourceBundle.getString("graphml.footer");

		String directed = graph.isDirected() ? "directed" : "undirected";
		String header = MessageFormat.format(headerFormat, directed);

		FileWriter fileWriter;
		try {
			fileWriter = new FileWriter("graph.graphml.xml");
			fileWriter.write(header);
			for (Vertex vertex : graph.getVertexSet()) {
				fileWriter.write(MessageFormat.format(nodeFormat, vertex.getId()));
			}
			int edgeId = 1;
			for (Edge edge : graph.getEdgeSet()) {
				int sourceId = edge.getTail().getId();
				int targetId = edge.getHead().getId();
				fileWriter.write(MessageFormat.format(edgeFormat, edgeId, sourceId, targetId));
				edgeId++;
			}
			fileWriter.write(footer);
			fileWriter.close();
		}
		catch (IOException e1) {
			ErrorDialog.showError(e1, null);
		}
	}
}
