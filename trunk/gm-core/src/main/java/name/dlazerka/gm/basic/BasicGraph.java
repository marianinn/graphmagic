/*
 * GraphMagic, package for scientists working in graph theory.
 * Copyright (C) 2009 Dzmitry Lazerka www.dlazerka.name
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
 * Author: Dzmitry Lazerka www.dlazerka.name
 */

package name.dlazerka.gm.basic;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import name.dlazerka.gm.Edge;
import name.dlazerka.gm.Graph;
import name.dlazerka.gm.GraphUI;
import name.dlazerka.gm.Vertex;
import name.dlazerka.gm.exception.DuplicateEdgeException;
import name.dlazerka.gm.exception.EdgeAddingException;
import name.dlazerka.gm.exception.EdgeCreateException;
import name.dlazerka.gm.exception.NoSuchEdgeException;
import name.dlazerka.gm.exception.NoSuchVertexException;
import name.dlazerka.gm.exception.PseudoEdgeForNonPseudoGraphException;
import name.dlazerka.gm.util.LinkedSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Dzmitry Lazerka www.dlazerka.name
 */
public class BasicGraph implements Graph, Serializable, Cloneable {
	private static final Logger logger = LoggerFactory.getLogger(BasicGraph.class);

	private final Set<Vertex> vertexSet = new LinkedHashSet<Vertex>();

	private final Set<Edge> edgeSet = new LinkedHashSet<Edge>();
	private GraphUI ui = new GraphUI();

	/**
	 * See {@link Graph#isDirected()}
	 */
	private final boolean directed;

	/**
	 * See {@link Graph#isMulti()}
	 */
	private final boolean multi;

	/**
	 * See {@link Graph#isPseudo()}
	 */
	private final boolean pseudo;

	/**
	 * A list of observers that are notified on graph changes.
	 * Not a subject for serialization.
	 */
	protected final transient List<GraphModificationListener> modificationListenerList = new LinkedList<GraphModificationListener>();

	/**
	 * Helper map for fast id->vertex lookup.
	 * Not a subject for serialization.
	 */
	private final transient HashMap<String, Vertex> idToVertex = new HashMap<String, Vertex>();

	public BasicGraph() {
		this(false, false, false);
	}

	public BasicGraph(boolean directed) {
		this(directed, false, false);
	}

	public BasicGraph(boolean directed, boolean multi, boolean pseudo) {
		this.directed = directed;
		this.multi = multi;
		this.pseudo = pseudo;
	}

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
	public Edge getEdge(Vertex source, Vertex target) {
		for (Edge edge : edgeSet) {
			if (edge.getSource().equals(source)
				&& edge.getTarget().equals(target)) {
				return edge;
			}

			if (!isDirected()
				&& edge.getSource().equals(target)
				&& edge.getTarget().equals(source)
			) {
				return edge;
			}
		}

		throw new NoSuchEdgeException(this, source, target);
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
		return createEdge(source, target, isDirected());
	}

	@Override
	public BasicEdge createEdge(Vertex source, Vertex target, boolean directed) throws EdgeCreateException {
		BasicEdge edge = new BasicEdge(this, source, target, directed);
		try {
			addEdge(edge);
		}
		catch (EdgeAddingException e) {
			throw new EdgeCreateException(source, target, e);
		}
		return edge;
	}

	/**
	 * Creates edge with default directed property.
	 */
	@Override
	public BasicEdge createEdge(String sourceId, String targetId) throws EdgeCreateException {
		return createEdge(sourceId, targetId, isDirected());
	}

	@Override
	public BasicEdge createEdge(String sourceId, String targetId, boolean directed) throws EdgeCreateException {
		Vertex source = getVertex(sourceId);
		Vertex target = getVertex(targetId);

		return createEdge(source, target, directed);
	}

	@Override
	public Edge createEdge(int sourceId, int targetId) throws EdgeCreateException {
		return createEdge(sourceId, targetId, isDirected());
	}

	@Override
	public Edge createEdge(int sourceId, int targetId, boolean directed) throws EdgeCreateException {
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
		return ui;
	}

	@Override
	public void setUI(GraphUI uI) {
		this.ui = uI;
	}

	public boolean isDirected() {
		return directed;
	}

	public boolean isMulti() {
		return multi;
	}

	public boolean isPseudo() {
		return pseudo;
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

		for (GraphModificationListener listener : modificationListenerList) {
			listener.edgeAdded(edge);
		}
	}

	public boolean addChangeListener(GraphModificationListener listener) {
		boolean result = modificationListenerList.add(listener);
		listener.notifyAttached();
		return result;
	}

	public void mergeFrom(Graph graph) throws MergeException {
		for (Vertex vertexFrom : graph.getVertexSet()) {
			BasicVertex vertexTo = createVertex(vertexFrom.getId());
			vertexTo.mergeFrom(vertexFrom);
		}
		for (Edge edgeFrom : graph.getEdgeSet()) {
			Vertex source = edgeFrom.getSource();
			Vertex target = edgeFrom.getTarget();
			try {
				BasicEdge edgeTo = createEdge(source.getId(), target.getId());
				edgeTo.mergeFrom(edgeFrom);
			}
			catch (EdgeCreateException e) {
				throw new MergeException(e);
			}
		}
	}
}
