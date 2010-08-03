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
	private final String id;
	private final Visual visual;
	private final Mark mark;
	private boolean removed = false;

	/**
	 * Protected constructor. To obtain new instance see {@link Graph#createVertex()}.
	 *
	 * @param graph graph containing the vertex
	 * @param id number that indentifies this vertex in its graph.  @see Graph#createVertex()
	 */
	protected BasicVertex(Graph graph, String id) {
		if (id == null) {
			throw new NullPointerException("ID cannot be null");
		}
		this.graph = graph;
		this.id = id;
		this.visual = new Visual();
		this.mark = new Mark();
	}

	@Override
	public String toString() {
		return id;
	}

	@Override
	public Graph getGraph() {
		checkNotRemoved();

		return graph;
	}

	public String getId() {
		checkNotRemoved();
		return id;
	}

	@Override
	public Visual getVisual() {
		checkNotRemoved();

		return visual;
	}

	@Override
	public Mark getMark() {
		return mark;
	}

	@Override
	public Set<Vertex> getAdjacentVertexSet() {
		checkNotRemoved();

		Set<Vertex> adjacentVertexSet = new LinkedSet<Vertex>();

		for (Edge edge : getIncidentEdgeSet()) {
			if (edge.getSource().equals(this)) {
				adjacentVertexSet.add(edge.getTarget());
			} else {
				adjacentVertexSet.add(edge.getSource());
			}
		}

		return adjacentVertexSet;
	}

	@Override
	public Set<Edge> getIncidentEdgeSet() {
		checkNotRemoved();

		Set<Edge> incidentEdgeSet = new LinkedSet<Edge>();

		for (Edge edge : graph.getEdgeSet()) {
			if (this.equals(edge.getTarget()) ||
					this.equals(edge.getSource())) {
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
	protected void setRemoved() {
		checkNotRemoved();

		removed = false;
	}

	protected void checkNotRemoved() {
		if (removed) {
			throw new VertexRemovedException();
		}
	}

	@Override
	public void mergeFrom(Vertex vertex) {
		getMark().putAll(vertex.getMark());
		getVisual().mergeFrom(vertex.getVisual());
	}
}
