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


import name.dlazerka.gm.Edge;
import name.dlazerka.gm.Graph;
import name.dlazerka.gm.Vertex;
import name.dlazerka.gm.basic.BasicGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Dzmitry Lazerka www.dlazerka.name
 */
public class GraphImpl extends BasicGraph implements Graph {
	private static final Logger logger = LoggerFactory.getLogger(GraphImpl.class);

	private final List<GraphChangeListener> changeListeners = new LinkedList<GraphChangeListener>();

	public GraphImpl() {
		create5();
	}

	public boolean addChangeListener(GraphChangeListener listener) {
		boolean result = changeListeners.add(listener);
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

		for (GraphChangeListener listener : changeListeners) {
			listener.vertexAdded(vertex);
		}
	}

	public void remove(Vertex vertex) {
		super.remove(vertex);

		for (GraphChangeListener changeListener : changeListeners) {
			changeListener.vertexDeleted(vertex);
		}
	}

	protected void addEdge(Edge edge) {
		super.addEdge(edge);

		for (GraphChangeListener listener : changeListeners) {
			listener.edgeAdded(edge);
		}
	}

	public void remove(Edge edge) {
		super.remove(edge);

		for (GraphChangeListener changeListener : changeListeners) {
			changeListener.edgeDeleted(edge);
		}
	}
}
