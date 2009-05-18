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
import name.dlazerka.gm.util.LinkedSet;

import java.io.Serializable;
import java.util.Set;

/**
 * @author Dzmitry Lazerka www.dlazerka.name
 */
public class BasicVertex extends AbstractVertex implements Vertex, Serializable {
	private final Graph graph;
	private final int label;
	private final Visual visual;
	private boolean removed = false;

	/**
	 * Protected constructor. To obtain new instance see {@link Graph#createVertex()}.
	 *
	 * @param graph
	 * @param label number that indentifies this vertex in its graph.  @see Graph#createVertex()
	 */
	protected BasicVertex(Graph graph, int label) {
		this.graph = graph;
		this.label = label;
		this.visual = new Visual();
	}

	@Override
	public String toString() {
		return label + "";
	}

	@Override
	public Graph getGraph() {
		checkNotRemoved();

		return graph;
	}

	public int getId() {
		checkNotRemoved();

		return label;
	}

	@Override
	public Visual getVisual() {
		checkNotRemoved();

		return visual;
	}

	@Override
	public Set<Vertex> getAdjacentVertexSet() {
		checkNotRemoved();

		Set<Vertex> adjacentVertexSet = new LinkedSet<Vertex>();

		for (Edge edge : getIncidentEdgeSet()) {
			if (edge.getTail().equals(this)) {
				adjacentVertexSet.add(edge.getHead());
			} else {
				adjacentVertexSet.add(edge.getTail());
			}
		}

		return adjacentVertexSet;
	}

	@Override
	public Set<Edge> getIncidentEdgeSet() {
		checkNotRemoved();

		Set<Edge> incidentEdgeSet = new LinkedSet<Edge>();

		for (Edge edge : graph.getEdgeSet()) {
			if (this.equals(edge.getHead()) ||
					this.equals(edge.getTail())) {
				incidentEdgeSet.add(edge);
			}
		}

		return incidentEdgeSet;
	}

	@Override
	public boolean isAdjacent(Vertex vertex) {
		checkNotRemoved();

		return getAdjacentVertexSet().contains(vertex);
	}

	@Override
	public boolean isIncident(Edge edge) {
		checkNotRemoved();

		return getIncidentEdgeSet().contains(edge);
	}

	/**
	 * Calling this tells that this vertex has been removed from its graph.
	 * This means that no use is expected anymore.
	 */
	protected void markRemoved() {
		checkNotRemoved();

		removed = false;
	}

	protected void checkNotRemoved() {
		if (removed) {
			throw new VertexRemovedException();
		}
	}
}
