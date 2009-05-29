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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Set;

/**
 * @author Dzmitry Lazerka www.dlazerka.name
 */
public class BasicEdge extends AbstractEdge implements Edge, Serializable {
	private static final Logger logger = LoggerFactory.getLogger(BasicEdge.class);

	private final Graph graph;
	private final Vertex tail;
	private final Vertex head;
	private final Visual visual = new Visual();
	private final Mark mark = new Mark();

	private boolean removed = false;

	protected BasicEdge(Graph graph, Vertex tail, Vertex head) {
		this.graph = graph;
		this.tail = tail;
		this.head = head;
	}

	@Override
	public String toString() {
		return "(" + tail + " -> " + head + ")";
	}

	@Override
	public Graph getGraph() {
		checkNotRemoved();

		return graph;
	}

	@Override
	public Vertex getTail() {
		checkNotRemoved();

		return tail;
	}

	@Override
	public Vertex getHead() {
		checkNotRemoved();

		return head;
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
	public Set<Edge> getIncidentEdgeSet() {
		checkNotRemoved();

		Set<Edge> incidentEdgeSet = new LinkedSet<Edge>();

		incidentEdgeSet.addAll(getTail().getIncidentEdgeSet());
		incidentEdgeSet.addAll(getHead().getIncidentEdgeSet());
		incidentEdgeSet.remove(this);

		return incidentEdgeSet;
	}

	@Override
	public boolean isIncident(Vertex vertex) {
		checkNotRemoved();

		return vertex.equals(getHead())
		       || vertex.equals(getTail());
	}

	@Override
	public boolean isIncident(Edge edge) {
		checkNotRemoved();

		return getIncidentEdgeSet().contains(edge);
	}

	@Override
	public boolean isPseudo() {
		checkNotRemoved();

		return tail.equals(head);
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
			throw new EdgeRemovedException();
		}
	}

	/**
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;

		BasicEdge edge = (BasicEdge) o;

		if (removed != edge.removed) return false;
		if (graph != null ? !graph.equals(edge.graph) : edge.graph != null) return false;

		if (graph != null && graph.isMulti()) {
			return false;
		}

		boolean result = true;
		if (head != null ? !head.equals(edge.head) : edge.head != null) result = false;
		if (tail != null ? !tail.equals(edge.tail) : edge.tail != null) result = false;

		if (!result && graph != null && !graph.isDirected()) {
			result = true;
			if (head != null ? !head.equals(edge.tail) : edge.tail != null) return false;
			if (tail != null ? !tail.equals(edge.head) : edge.head != null) return false;
		}

		return result;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (graph != null ? graph.hashCode() : 0);
		result = 31 * result + (tail != null ? tail.hashCode() : 0);
		result = 31 * result + (head != null ? head.hashCode() : 0);
		result = 31 * result + (removed ? 1 : 0);
		return result;
	}
}
