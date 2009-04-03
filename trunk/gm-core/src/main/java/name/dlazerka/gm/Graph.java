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

import java.util.Set;

/**
 * @author Dzmitry Lazerka www.dlazerka.name
 */
public interface Graph {
	/**
	 * Return the number of vertices: <code>|V|</code>.
	 * @return the number of vertices.
	 */
	int getOrder();

	/**
	 * Return the number of edges: <code>|E|</code>.
	 * @return the number of edges.
	 */
	int getSize();

	Set<Vertex> getVertexSet();

	Set<Edge> getEdgeSet();

	Vertex getVertex(int id);

	Edge getEdge(int tailId, int headId);

	Vertex createVertex();

	Edge createEdge(Vertex tail, Vertex head);

	Edge createEdge(int tailId, int headId);

	void remove(Vertex vertex);

	void remove(Edge edge);

	void clear();

	GraphUI getUI();
	
	void setUI(GraphUI uI);
}
