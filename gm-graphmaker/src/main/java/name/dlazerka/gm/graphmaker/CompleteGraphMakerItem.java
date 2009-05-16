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

package name.dlazerka.gm.graphmaker;

import name.dlazerka.gm.Graph;
import name.dlazerka.gm.GraphMagicAPI;
import name.dlazerka.gm.Vertex;
import name.dlazerka.gm.Visual;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;

/**
 * @author Dzmitry Lazerka www.dlazerka.name
 */
public class CompleteGraphMakerItem extends GraphMakerItem {
	private static final Logger logger = LoggerFactory.getLogger(BipartiteGraphMakerItem.class);
	protected JTextField nField;

	public CompleteGraphMakerItem(GraphMagicAPI graphMagicAPI) {
		super(graphMagicAPI);
	}

	@Override
	public String getLabel() {
		return "Complete(n)";
	}

	@Override
	protected void perform() {
		String nText = nField.getText();
		Integer n = Integer.valueOf(nText);

		createAndConnect(n);
	}

	private void createAndConnect(int n) {
		Graph graph = getGraphMagicAPI().getFocusedGraph();
		graph.clear();

		CycleIterator cycleIterator = new CycleIterator(n);

		while (cycleIterator.hasNext()) {
			Point2D point2D = cycleIterator.next();

			Vertex vertex = graph.createVertex();
			Visual visual = vertex.getVisual();

			visual.setCenter(point2D.getX(), point2D.getY());


			int id = vertex.getId();
			for (int i = 1; i < id; i++) {
				graph.createEdge(i, id);
			}
		}
	}

	@Override
	public void fillParamsPanel(JPanel panel) {
		logger.debug("");

		nField = new JTextField(10);
		panel.add(nField, BorderLayout.WEST);

		nField.setText("5");

		nField.selectAll();
		nField.requestFocusInWindow();
	}
}
