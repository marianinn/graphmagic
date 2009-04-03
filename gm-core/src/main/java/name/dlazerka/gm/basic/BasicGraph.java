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
import name.dlazerka.gm.LabeledGraph;
import name.dlazerka.gm.Vertex;
import name.dlazerka.gm.util.LinkedSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

/**
 * @author Dzmitry Lazerka www.dlazerka.name
 */
public class BasicGraph implements LabeledGraph {
	private static final Logger logger = LoggerFactory.getLogger(BasicGraph.class);

	private final Set<Vertex> vertexSet = new LinkedSet<Vertex>();

	private final Set<Edge> edgeSet = new LinkedSet<Edge>();

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
		return vertexSet;
	}

	@Override
	public Set<Edge> getEdgeSet() {
		return edgeSet;
	}

	@Override
	public Vertex createVertex() {
		int max = 0;
		for (Vertex vertex : vertexSet) {
			if (vertex.getNumber() > max) {
				max = vertex.getNumber();
			}
		}
		Vertex vertex = new BasicVertex(this, max + 1);

		addVertex(vertex);

		return vertex;
	}

	@Override
	public Edge createEdge(Vertex tail, Vertex head) {
		BasicEdge edge = new BasicEdge(tail, head);
		addEdge(edge);
		return edge;
	}

	@Override
	public Edge createEdge(int tailNum, int headNum) {
		Vertex tail = getVertex(tailNum);
		Vertex head = getVertex(headNum);
		return createEdge(tail, head);
	}

	@Override
	public Vertex getVertex(int num) {
		for (Vertex vertex : vertexSet) {
			if (vertex.getNumber() == num) {
				return vertex;
			}
		}

		throw new IllegalArgumentException("Graph does not contain vertex number " + num);
	}

	@Override
	public void remove(Vertex vertex) {
		Set<Edge> edgesToRemove = vertex.getAdjacentEdges();

		for (Edge edge : edgesToRemove) {
			remove(edge);
		}

		vertexSet.remove(vertex);
	}

	@Override
	public void remove(Edge edge) {
		edgeSet.remove(edge);
	}

	@Override
	public void clear() {
		LinkedSet<Vertex> tempSet = new LinkedSet<Vertex>(vertexSet);
		for (Vertex vertex : tempSet) {
			remove(vertex);
		}
	}

	protected void addVertex(Vertex vertex) {
		logger.trace("{}", vertex);
		vertexSet.add(vertex);
	}

	protected void addEdge(Edge edge) {
		logger.trace("{}", edge);
		edgeSet.add(edge);
	}
}
