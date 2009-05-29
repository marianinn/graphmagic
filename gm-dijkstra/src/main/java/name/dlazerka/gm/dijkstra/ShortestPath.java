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

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ShortestPath implements Serializable, Comparator<Vertex> {
	private Map<Vertex, Integer> currentShortest = new HashMap<Vertex, Integer>();
	private Map<Vertex, Edge> parent = new HashMap<Vertex, Edge>();

	protected void execute(Graph graph, Vertex startVertex, Map<Edge, Integer> weights) {
		dijkstraInit(graph, startVertex);

		Set<Vertex> vertexSet = graph.getVertexSet();
		DijkstraQueue vertexQueue = new VertexQueue(this);
		vertexQueue.addAll(vertexSet);

		while (!vertexQueue.isEmpty()) {
			Vertex minVertex = vertexQueue.extractMin();

			Set<Vertex> adjacentVertexSet = minVertex.getAdjacentVertexSet();
			for (Vertex adjacentVertex : adjacentVertexSet) {
				relax(graph, weights, vertexQueue, minVertex, adjacentVertex);
			}

			Mark mark = minVertex.getMark();
			mark.setAt(0, String.valueOf(currentShortest.get(minVertex)));
			Edge parentEdge = parent.get(minVertex);
			if (parentEdge != null) {
				Visual edgeVisual = parentEdge.getVisual();
				edgeVisual.setSelected(true);
			}
		}
	}

	protected void relax(
		Graph graph,
		Map<Edge, Integer> weights,
		DijkstraQueue vertexQueue,
		Vertex minVertex,
		Vertex adjacentVertex
	) {
		Edge edge = graph.getEdge(minVertex, adjacentVertex);
		Integer dist = weights.get(edge);
		if (currentShortest.get(adjacentVertex) > currentShortest.get(minVertex) + dist) {
			currentShortest.put(adjacentVertex, currentShortest.get(minVertex) + dist);
			vertexQueue.update(adjacentVertex);
			parent.put(adjacentVertex, edge);
		}
	}

	private void dijkstraInit(Graph graph, Vertex startVertex) {
		for (Vertex vertex : graph.getVertexSet()) {
			currentShortest.put(vertex, Integer.MAX_VALUE);
			parent.put(vertex, null);
		}
		currentShortest.put(startVertex, 0);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int compare(Vertex v1, Vertex v2) {
		Integer d1 = currentShortest.get(v1);
		Integer d2 = currentShortest.get(v2);

		if (d1 == null && d2 == null) {
			return 0;
		}

		if (d1 == null) {
			return 1;
		}

		if (d2 == null) {
			return -1;
		}

		return d1 - d2;
	}
}