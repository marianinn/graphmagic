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
	 * @param id starts from 1
	 * @return vertex
	 */
	Vertex getVertex(int id);

	Edge getEdge(Vertex vertex1, Vertex vertex2) throws MultipleEdgesException;

	Edge getEdge(int tailId, int headId) throws MultipleEdgesException;

	Set<Edge> getEdgesBetween(Vertex vertex1, Vertex vertex2);

	Set<Edge> getEdgesBetween(int tailId, int headId);

	/**
	 * A vertex labeling is a function from some subset of the integers to the vertices of the graph.
	 *
	 * @return vertex labeling map
	 */
	Map<String, Vertex> getVertexLabeling();

	/**
	 * An edge labeling is a function from some subset of the integers to the edges of the graph.
	 *
	 * @return edge labeling map
	 */
	Map<String, Edge> getEdgeLabeling();

	Vertex createVertex();

	Edge createEdge(Vertex tail, Vertex head) throws EdgeCreateException;

	Edge createEdge(int tailId, int headId) throws EdgeCreateException;

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
	 * @param directed see {@link #isDirected()}
	 */
	void setDirected(boolean directed);

	/**
	 * @return is this graph a multigraph,
	 *         i.e. does it permit to have more than one edge with the same tail and head vertices.
	 */
	boolean isMulti();

	/**
	 * @param multi see {@link #isMulti()}
	 */
	void setMulti(boolean multi);

	/**
	 * @return is this graph a pseudograph,
	 *         i.e. does it permit to have a loop: edge that connects a vertex to itself.
	 */
	boolean isPseudo();

	/**
	 * @param pseudo see {@link #isPseudo()}
	 */
	void setPseudo(boolean pseudo);
}
