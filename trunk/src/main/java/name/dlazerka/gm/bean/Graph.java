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

package name.dlazerka.gm.bean;


import name.dlazerka.gm.util.LinkedSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @author Dzmitry Lazerka www.dlazerka.name
 */
public class Graph {
	private static final Logger logger = LoggerFactory.getLogger(Graph.class);

	private final Set<Vertex> vertexSet = new LinkedSet<Vertex>();
	private final Set<Edge> edgeSet = new LinkedSet<Edge>();
	private final List<GraphChangeListener> changeListeners = new LinkedList<GraphChangeListener>();

	public Graph() {
		create5();
	}

	public boolean addChangeListener(GraphChangeListener listener) {
		boolean result = changeListeners.add(listener);
		listener.notifyAttached();
		return result;
	}

	private void create1() {
		Vertex vertex1 = new Vertex(1);

		add(vertex1);
	}

	private void create2() {
		Vertex vertex1 = new Vertex(1);
		Vertex vertex2 = new Vertex(2);

		add(vertex1);
		add(vertex2);

		Edge edge1 = new Edge(vertex1, vertex2);
		add(edge1);
	}

	private void create4() {
		Vertex vertex1 = new Vertex(1);
		Vertex vertex2 = new Vertex(2);
		Vertex vertex3 = new Vertex(3);
		Vertex vertex4 = new Vertex(4);

		add(vertex1);
		add(vertex2);
		add(vertex3);
		add(vertex4);

		Edge edge12 = new Edge(vertex1, vertex2);
		Edge edge23 = new Edge(vertex2, vertex3);
		Edge edge31 = new Edge(vertex3, vertex1);
		Edge edge34 = new Edge(vertex3, vertex4);
		Edge edge41 = new Edge(vertex4, vertex1);

		add(edge12);
		add(edge23);
		add(edge31);
		add(edge34);
		add(edge41);
	}

	private void create5() {
		create4();
		Vertex vertex5 = new Vertex(5);

		add(vertex5);


		Edge edge42 = new Edge(new Vertex(4), new Vertex(2));
		add(edge42);

		Edge edge51 = new Edge(new Vertex(5), new Vertex(1));
		Edge edge52 = new Edge(new Vertex(5), new Vertex(2));
		Edge edge53 = new Edge(new Vertex(5), new Vertex(3));
		Edge edge54 = new Edge(new Vertex(5), new Vertex(4));

		add(edge51);
		add(edge52);
		add(edge53);
		add(edge54);
	}

	public Set<Vertex> getVertexSet() {
		return vertexSet;
	}

	public Set<Edge> getEdgeSet() {
		return edgeSet;
	}
	public Vertex add(Vertex vertex) {
		logger.trace("{}", vertex);

		vertexSet.add(vertex);

		for (GraphChangeListener listener : changeListeners) {
			listener.vertexAdded(vertex);
		}

		return vertex;
	}

	/**
	 * Firstly removes adjacent edges one by one, then removes the vertex notifying {@link #changeListeners}
	 * @param vertex vertex to remove
	 */
	public void remove(Vertex vertex) {
		while (vertex.getAdjacentEdgeSet().size() != 0) {
			Edge edge = vertex.getAdjacentEdgeSet().iterator().next();
			remove(edge);
		}

		vertexSet.remove(vertex);

		for (GraphChangeListener changeListener : changeListeners) {
			changeListener.vertexDeleted(vertex);
		}
	}

	public void add(Edge edge) {
		logger.trace("{}", edge);

		edgeSet.add(edge);

		for (GraphChangeListener listener : changeListeners) {
			listener.edgeAdded(edge);
		}
	}

	public void remove(Edge edge) {
		edge.getHead().getAdjacentEdgeSet().remove(edge);
		edge.getTail().getAdjacentEdgeSet().remove(edge);

		edgeSet.remove(edge);

		for (GraphChangeListener changeListener : changeListeners) {
			changeListener.edgeDeleted(edge);
		}
	}

	public Vertex addVertex() {
		int max = 0;
		for (Vertex vertex : vertexSet) {
			if (vertex.getNumber() > max) {
				max = vertex.getNumber();
			}
		}
		Vertex vertex = new Vertex(max + 1);

		return add(vertex);
	}
}
