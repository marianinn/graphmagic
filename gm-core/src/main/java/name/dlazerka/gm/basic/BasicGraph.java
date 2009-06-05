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

import name.dlazerka.gm.*;
import name.dlazerka.gm.exception.*;
import name.dlazerka.gm.util.LinkedSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.*;

/**
 * @author Dzmitry Lazerka www.dlazerka.name
 */
public class BasicGraph implements Graph, Serializable {
	private static final Logger logger = LoggerFactory.getLogger(BasicGraph.class);

	private final Set<Vertex> vertexSet = new LinkedSet<Vertex>();

	private final Set<Edge> edgeSet = new LinkedSet<Edge>();
	private GraphUI uI = new GraphUI();

	/**
	 * See {@link Graph#isDirected()}
	 */
	private boolean directed;

	/**
	 * See {@link Graph#isMulti()}
	 */
	private boolean multi;

	/**
	 * See {@link Graph#isPseudo()}
	 */
	private boolean pseudo;

	/**
	 * A list of observers that are notified on graph changes.
	 */
	protected final List<GraphModificationListener> modificationListenerList = new LinkedList<GraphModificationListener>();

	private Map<String, Vertex> vertexLabeling = new HashMap<String, Vertex>();

	private Map<String, Edge> edgeLabeling = new HashMap<String, Edge>();


	@Override
	public String toString() {
		return "Graph{" + vertexSet + ", " + edgeSet + '}';
	}

	@Override
	public int getOrder() {
		return vertexSet.size();
	}

	@Override
	public int getSize() {
		return edgeSet.size();
	}

	@Override
	public Set<Vertex> getVertexSet() {
		return Collections.unmodifiableSet(vertexSet);
	}

	@Override
	public Set<Edge> getEdgeSet() {
		return Collections.unmodifiableSet(edgeSet);
	}

	@Override
	public Vertex getVertex(int id) {
		for (Vertex vertex : vertexSet) {
			if (vertex.getId() == id) {
				return vertex;
			}
		}

		throw new NoSuchVertexException(this, id);
	}

	@Override
	public Edge getEdge(Vertex tail, Vertex head) {
		for (Edge edge : edgeSet) {
			if (edge.getTail().equals(tail)
			    && edge.getHead().equals(head)) {
				return edge;
			}

			if (!isDirected()
			    && edge.getTail().equals(head)
			    && edge.getHead().equals(tail)
				) {
				return edge;
			}
		}

		throw new NoSuchEdgeException(this, tail, head);
	}

	@Override
	public Edge getEdge(int tailId, int headId) {
		Vertex tail = getVertex(tailId);
		Vertex head = getVertex(headId);

		return getEdge(tail, head);
	}

	@Override
	public Set<Edge> getEdgesBetween(Vertex vertex1, Vertex vertex2) {
		throw new UnsupportedOperationException("TODO");
	}

	@Override
	public Set<Edge> getEdgesBetween(int tailId, int headId) {
		throw new UnsupportedOperationException("TODO");
	}

	@Override
	public Map<String, Vertex> getVertexLabeling() {
		return vertexLabeling;
	}

	@Override
	public Map<String, Edge> getEdgeLabeling() {
		return edgeLabeling;
	}

	@Override
	public BasicVertex createVertex() {
		int max = 0;
		for (Vertex vertex : vertexSet) {
			if (vertex.getId() > max) {
				max = vertex.getId();
			}
		}
		BasicVertex vertex = new BasicVertex(this, max + 1);

		addVertex(vertex);

		return vertex;
	}

	@Override
	public BasicEdge createEdge(Vertex tail, Vertex head) throws EdgeCreateException {
		BasicEdge edge = new BasicEdge(this, tail, head);
		try {
			addEdge(edge);
		}
		catch (EdgeAddingException e) {
			throw new EdgeCreateException(tail, head, e);
		}
		return edge;
	}

	@Override
	public BasicEdge createEdge(int tailId, int headId) throws EdgeCreateException {
		Vertex tail = getVertex(tailId);
		Vertex head = getVertex(headId);

		return createEdge(tail, head);
	}

	@Override
	public void remove(Vertex vertex) {
		logger.debug("{}", vertex);
		Set<Edge> edgesToRemove = vertex.getIncidentEdgeSet();

		for (Edge edge : edgesToRemove) {
			remove(edge);
		}

		boolean contained = vertexSet.remove(vertex);

		if (!contained) {
			throw new NoSuchVertexException(this, vertex);
		}

		if (vertex instanceof BasicVertex) {
			((BasicVertex) vertex).markRemoved();
		}

		for (GraphModificationListener modificationListener : modificationListenerList) {
			modificationListener.vertexDeleted(vertex);
		}
	}

	@Override
	public void remove(Edge edge) {
		logger.debug("{}", edge);
		boolean contained = edgeSet.remove(edge);

		if (!contained) {
			throw new NoSuchEdgeException(this, edge);
		}

		if (edge instanceof BasicEdge) {
			((BasicEdge) edge).markRemoved();
		}

		for (GraphModificationListener modificationListener : modificationListenerList) {
			modificationListener.edgeDeleted(edge);
		}
	}

	@Override
	public void clear() {
		LinkedSet<Vertex> tempSet = new LinkedSet<Vertex>(vertexSet);
		for (Vertex vertex : tempSet) {
			remove(vertex);
		}
	}

	@Override
	public GraphUI getUI() {
		return uI;
	}

	@Override
	public void setUI(GraphUI uI) {
		this.uI = uI;
	}

	public boolean isDirected() {
		return directed;
	}

	@Override
	public void setDirected(boolean directed) {
		logger.debug("{}", directed);
		this.directed = directed;
	}

	public boolean isMulti() {
		return multi;
	}

	@Override
	public void setMulti(boolean multi) {
		logger.debug("{}", multi);
		this.multi = multi;
	}

	public boolean isPseudo() {
		return pseudo;
	}

	@Override
	public void setPseudo(boolean pseudo) {
		logger.debug("{}", pseudo);
		this.pseudo = pseudo;
	}

	protected void addVertex(Vertex vertex) {
		logger.debug("{}", vertex);
		vertexSet.add(vertex);

		vertexLabeling.put(vertex.toString(), vertex);

		for (GraphModificationListener listener : modificationListenerList) {
			listener.vertexAdded(vertex);
		}
	}

	protected void addEdge(Edge edge) throws EdgeAddingException {
		logger.debug("{}", edge);

		if (!isPseudo() && edge.isPseudo()) {
			throw new PseudoEdgeForNonPseudoGraphException();
		}

		if (!edgeSet.add(edge)) {
			throw new DuplicateEdgeException(edge);
		}

		edgeLabeling.put(edge.toString(), edge);

		for (GraphModificationListener listener : modificationListenerList) {
			listener.edgeAdded(edge);
		}
	}

	public boolean addChangeListener(GraphModificationListener listener) {
		boolean result = modificationListenerList.add(listener);
		listener.notifyAttached();
		return result;
	}
}
