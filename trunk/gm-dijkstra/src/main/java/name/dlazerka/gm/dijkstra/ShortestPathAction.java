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

package name.dlazerka.gm.dijkstra;

import name.dlazerka.gm.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Dzmitry Lazerka www.dlazerka.name
 */
class ShortestPathAction extends AbstractAction {
    private static final Logger logger = LoggerFactory.getLogger(ShortestPathAction.class);

    private final GraphMagicAPI api;
	private static final int WEIGHT_KEY_0 = 0;
	private static final String WEIGHT_KEY_1 = "w";
	private static final String WEIGHT_KEY_2 = "weight";

	ShortestPathAction(GraphMagicAPI api) {
        super("Shortest path for single source");
        this.api = api;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Graph graph = api.getFocusedGraph();
        try {
            Vertex startVertex = findStartVertex(graph);
            ShortestPath algorithm = new ShortestPath();
	        Map<Edge, Integer> weights = computeWeights(graph);
	        algorithm.execute(graph, startVertex, weights);
        }
        catch (PluginException e1) {
            api.showMessage(e1, MessageLevel.ERROR);
        }
    }

	private Map<Edge, Integer> computeWeights(Graph graph) {
		Set<Edge> edges = graph.getEdgeSet();
		Map<Edge,Integer> w = new HashMap<Edge, Integer>(edges.size());
		for (Edge edge : edges) {
			Mark edgeMark = edge.getMark();
			String weightStr = (String) edgeMark.get(WEIGHT_KEY_0);
			if (weightStr == null) {
				weightStr = (String) edgeMark.get(WEIGHT_KEY_1);
			}
			if (weightStr == null) {
				weightStr = (String) edgeMark.get(WEIGHT_KEY_2);
			}

			if (weightStr == null) {
				weightStr = "1";
			}

			Integer weight = Integer.valueOf(weightStr);
			w.put(edge, weight);
		}
		return w;
	}

	private Vertex findStartVertex(Graph graph) throws NoStartVertexException {
        Set<Vertex> vertexSet = graph.getVertexSet();
        Vertex startVertex = null;
        for (Vertex vertex : vertexSet) {
            Visual visual = vertex.getVisual();
            boolean selected = visual.isSelected();
            if (selected) {
                if (startVertex == null) {
                    startVertex = vertex;
                }
                else {
                    throw new MultipleStartVertexException();
                }
            }
        }

        if (startVertex == null) {
                throw new NoStartVertexException();
        }

        return startVertex;
    }
}
