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

package name.dlazerka.gm;

import name.dlazerka.gm.basic.GraphModificationListener;
import name.dlazerka.gm.basic.MergeException;
import name.dlazerka.gm.exception.EdgeCreateException;
import name.dlazerka.gm.exception.MultipleEdgesException;

import java.util.Map;
import java.util.Set;

/**
 * @author Dzmitry Lazerka www.dlazerka.name
 */
public interface Graph {
	/**
	 * Registers a {@link GraphModificationListener} that will be notified on graph changes
	 * (e.g. vertex created, toggled directed/undirected}
	 *
	 * @param listener listener
	 * @return true
	 */
	boolean addChangeListener(GraphModificationListener listener);

	/**
	 * Return the number of vertices: <code>|V|</code>.
	 *
	 * @return the number of vertices.
	 */
	int getOrder();

	/**
	 * Return the number of edges: <code>|E|</code>.
	 *
	 * @return the number of edges.
	 */
	int getSize();

	Set<Vertex> getVertexSet();

	Set<Edge> getEdgeSet();

	/**
	 * Returns vertex by ID.
	 *
	 * @param id (by default starts from 1)
	 * @return vertex
	 */
	Vertex getVertex(String id);

	Edge getEdge(Vertex source, Vertex target) throws MultipleEdgesException;

	Edge getEdge(String sourceId, String targetId) throws MultipleEdgesException;

	Set<Edge> getEdgesBetween(Vertex vertex1, Vertex vertex2);

	Set<Edge> getEdgesBetween(String sourceId, String targetId);

	/**
	 * An edge labeling is a function from some subset of the integers to the edges of the graph.
	 *
	 * @return edge labeling map
	 */
	Map<String, Edge> getEdgeLabeling();

	Vertex createVertex();

	Vertex createVertex(int id);

	Vertex createVertex(String id);

	Edge createEdge(Vertex source, Vertex target) throws EdgeCreateException;

	Edge createEdge(Vertex source, Vertex target, boolean directed) throws EdgeCreateException;

	Edge createEdge(String sourceId, String targetId) throws EdgeCreateException;

	Edge createEdge(String sourceId, String targetId, boolean directed) throws EdgeCreateException;

	Edge createEdge(int sourceId, int targetId) throws EdgeCreateException;

	Edge createEdge(int sourceId, int targetId, boolean directed) throws EdgeCreateException;

	void remove(Vertex vertex);

	void remove(Edge edge);

	void clear();

	GraphUI getUI();

	void setUI(GraphUI uI);


	/**
	 * @return is this graph a directed graph,
	 *         i.e. are its edges directed.
	 */
	boolean isDirected();

	/**
	 * @return is this graph a multigraph,
	 *         i.e. does it permit to have more than one edge with the same tail and head vertices.
	 */
	boolean isMulti();

	/**
	 * @return is this graph a pseudograph,
	 *         i.e. does it permit to have a loop: edge that connects a vertex to itself.
	 */
	boolean isPseudo();

	/**
	 * Copies everything from given graph to this.
	 * @param graph merge from
	 * @throws name.dlazerka.gm.basic.MergeException in case merge fails
	 */
	void mergeFrom(Graph graph) throws MergeException;
}
