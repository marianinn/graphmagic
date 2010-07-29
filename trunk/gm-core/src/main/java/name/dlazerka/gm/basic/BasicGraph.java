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

	private final Set<Vertex> vertexSet = new LinkedHashSet<Vertex>();
	private final HashMap<String, Vertex> idToVertex = new HashMap<String, Vertex>();

	private final Set<Edge> edgeSet = new LinkedHashSet<Edge>();
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

	private Map<String, Edge> edgeLabeling = new HashMap<String, Edge>();

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof BasicGraph)) return false;

		BasicGraph that = (BasicGraph) o;

		if (directed != that.directed) return false;
		if (multi != that.multi) return false;
		if (pseudo != that.pseudo) return false;
		if (!edgeSet.equals(that.edgeSet)) return false;
		if (!vertexSet.equals(that.vertexSet)) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = vertexSet.hashCode();
		result = 31 * result + edgeSet.hashCode();
		result = 31 * result + (directed ? 1 : 0);
		result = 31 * result + (multi ? 1 : 0);
		result = 31 * result + (pseudo ? 1 : 0);
		return result;
	}

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
	public Vertex getVertex(String id) {
		Vertex vertex = idToVertex.get(id);
		if (vertex == null) {
			throw new NoSuchVertexException(this, id);
		}
		return vertex;
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
	public Edge getEdge(String sourceId, String targetId) {
		Vertex source = getVertex(sourceId);
		Vertex target = getVertex(targetId);

		return getEdge(source, target);
	}

	@Override
	public Set<Edge> getEdgesBetween(Vertex vertex1, Vertex vertex2) {
		throw new UnsupportedOperationException("TODO");
	}

	@Override
	public Set<Edge> getEdgesBetween(String sourceId, String targetId) {
		throw new UnsupportedOperationException("TODO");
	}

	@Override
	public Map<String, Edge> getEdgeLabeling() {
		return edgeLabeling;
	}

	@Override
	public BasicVertex createVertex() {
		int newId = 1;
		for (Vertex vertex : vertexSet) {
			String id = vertex.getId();
			if (id.equals(newId + "")) {
				newId++;
			}
			else {
				break;
			}
		}
		int id = newId;
		return createVertex(String.valueOf(id));
	}

	@Override
	public BasicVertex createVertex(String id) {
		BasicVertex vertex = new BasicVertex(this, id);
		addVertex(vertex);
		return vertex;
	}

	@Override
	public Vertex createVertex(int id) {
		return createVertex(id + "");
	}

	@Override
	public BasicEdge createEdge(Vertex source, Vertex target) throws EdgeCreateException {
		BasicEdge edge = new BasicEdge(this, source, target);
		try {
			addEdge(edge);
		}
		catch (EdgeAddingException e) {
			throw new EdgeCreateException(source, target, e);
		}
		return edge;
	}

	@Override
	public BasicEdge createEdge(String sourceId, String targetId) throws EdgeCreateException {
		Vertex source = getVertex(sourceId);
		Vertex target = getVertex(targetId);

		return createEdge(source, target);
	}

	@Override
	public Edge createEdge(int sourceId, int targetId) throws EdgeCreateException {
		return createEdge(sourceId + "", targetId + "");
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
			((BasicVertex) vertex).setRemoved();
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
			((BasicEdge) edge).setRemoved();
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

		idToVertex.put(vertex.toString(), vertex);

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
