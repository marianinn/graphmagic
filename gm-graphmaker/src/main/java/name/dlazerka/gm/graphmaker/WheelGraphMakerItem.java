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

import name.dlazerka.gm.GraphMagicAPI;
import name.dlazerka.gm.Graph;
import name.dlazerka.gm.Vertex;

/**
 * @author Dzmitry Lazerka www.dlazerka.name
 */
public class WheelGraphMakerItem extends CycleGraphMakerItem {
	public WheelGraphMakerItem(GraphMagicAPI graphMagicAPI) {
		super(graphMagicAPI);
	}

	@Override
	public String getLabel() {
		return "Wheel(n)";
	}

	@Override
	public void perform() {
		String nText = nField.getText();
		Integer n = Integer.valueOf(nText);

		createCycleConnect(n - 1);

		Graph graph = getGraphMagicAPI().getFocusedGraph();

		Vertex centerVertex = graph.createVertex();
		centerVertex.getVisual().setCenter(0, 0);

		for (Vertex vertex : graph.getVertexSet()) {
			graph.createEdge(centerVertex, vertex);
		}
	}
}
