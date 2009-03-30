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

package name.dlazerka.gm.basic;


import name.dlazerka.gm.Edge;
import name.dlazerka.gm.Graph;
import name.dlazerka.gm.Vertex;
import name.dlazerka.gm.ObservableGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Dzmitry Lazerka www.dlazerka.name
 */
public class ObservableBasicGraph extends BasicGraph implements Graph, ObservableGraph {
	private static final Logger logger = LoggerFactory.getLogger(ObservableBasicGraph.class);

	private final List<GraphModificationListener> modificationListenerList = new LinkedList<GraphModificationListener>();

	public ObservableBasicGraph() {
		create5();
	}

	public boolean addChangeListener(GraphModificationListener listener) {
		boolean result = modificationListenerList.add(listener);
		listener.notifyAttached();
		return result;
	}

	private void create1() {
		createVertex();
	}

	private void create2() {
		Vertex vertex1 = createVertex();
		Vertex vertex2 = createVertex();

		createEdge(vertex1, vertex2);
	}

	private void create4() {
		Vertex vertex1 = createVertex();
		Vertex vertex2 = createVertex();
		Vertex vertex3 = createVertex();
		Vertex vertex4 = createVertex();

		createEdge(vertex1, vertex2);
		createEdge(vertex2, vertex3);
		createEdge(vertex3, vertex1);
		createEdge(vertex3, vertex4);
		createEdge(vertex4, vertex1);
	}

	private void create5() {
		create4();
		createVertex();

		createEdge(4, 2);

		createEdge(5, 1);
		createEdge(5, 2);
		createEdge(5, 3);
		createEdge(5, 4);
	}


	public void addVertex(Vertex vertex) {
		super.addVertex(vertex);

		for (GraphModificationListener listener : modificationListenerList) {
			listener.vertexAdded(vertex);
		}
	}

	public void remove(Vertex vertex) {
		super.remove(vertex);

		for (GraphModificationListener modificationListener : modificationListenerList) {
			modificationListener.vertexDeleted(vertex);
		}
	}

	protected void addEdge(Edge edge) {
		super.addEdge(edge);

		for (GraphModificationListener listener : modificationListenerList) {
			listener.edgeAdded(edge);
		}
	}

	public void remove(Edge edge) {
		super.remove(edge);

		for (GraphModificationListener modificationListener : modificationListenerList) {
			modificationListener.edgeDeleted(edge);
		}
	}
}
