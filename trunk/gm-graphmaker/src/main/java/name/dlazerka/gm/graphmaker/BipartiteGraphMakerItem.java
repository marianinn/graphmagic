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
import name.dlazerka.gm.exception.EdgeCreateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;

/**
 * @author Dzmitry Lazerka www.dlazerka.name
 */
public class BipartiteGraphMakerItem extends GraphMakerItem {
	private static final Logger logger = LoggerFactory.getLogger(BipartiteGraphMakerItem.class);
	protected JTextField nField;
	protected JTextField kField;

	public BipartiteGraphMakerItem(GraphMagicAPI graphMagicAPI) {
		super(graphMagicAPI);
	}

	@Override
	public String getLabel() {
		return "Bipartite(n,k)";
	}

	@Override
	protected void perform() {
		String nText = nField.getText();
		String kText = kField.getText();
		Integer n = Integer.valueOf(nText);
		Integer k = Integer.valueOf(kText);

		createAndConnect(n, k);
	}

	private void createAndConnect(int n, int k) {
		Graph graph = getGraphMagicAPI().getFocusedGraph();
		graph.clear();

		double x = -1d / 3;
		for (int i = 0; i < n; i++) {
			Vertex vertex = graph.createVertex();
			Visual visual = vertex.getVisual();

			double y = (1d - n + 2 * i) / (n + 1);

			visual.setCenter(x, y);
		}

		x = 1d / 3;
		for (int i = 0; i < k; i++) {
			Vertex vertex = graph.createVertex();
			Visual visual = vertex.getVisual();

			double y = (1d - k + 2 * i) / (k + 1);

			visual.setCenter(x, y);

			try {
				for (int j = 0; j < n; j++) {
					graph.createEdge(j + 1, vertex.getId());
				}
			} catch (EdgeCreateException e) {
				// silently skip
			}
		}
	}

	@Override
	public void fillParamsPanel(JPanel panel) {
		logger.debug("");

		panel.setLayout(new BorderLayout());

		nField = new JTextField(10);
		kField = new JTextField(10);
		panel.add(nField, BorderLayout.WEST);
		panel.add(kField, BorderLayout.EAST);

		nField.setText("5");
		kField.setText("4");

		nField.selectAll();
		nField.requestFocusInWindow();
	}
}
