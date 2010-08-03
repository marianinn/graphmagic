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
	private final Vertex source;
	private final Vertex target;
	private final Visual visual = new Visual();
	private final Mark mark = new Mark();

	private boolean removed = false;
	private final boolean directed;

	protected BasicEdge(Graph graph, Vertex source, Vertex target, boolean directed) {
		this.graph = graph;
		this.source = source;
		this.target = target;
		this.directed = directed;
	}

	@Override
	public String toString() {
		if (isDirected()) {
			return "(" + source + ", " + target + ")";
		}
		else {
			return "{" + source + ", " + target + "}";
		}
	}

	@Override
	public Graph getGraph() {
		checkNotRemoved();

		return graph;
	}

	@Override
	public Vertex getSource() {
		return source;
	}

	@Override
	public Vertex getTarget() {
		return target;
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

		incidentEdgeSet.addAll(getSource().getIncidentEdgeSet());
		incidentEdgeSet.addAll(getTarget().getIncidentEdgeSet());
		incidentEdgeSet.remove(this);

		return incidentEdgeSet;
	}

	@Override
	public boolean isIncident(Vertex vertex) {
		checkNotRemoved();

		return vertex.equals(getTarget())
		       || vertex.equals(getSource());
	}

	@Override
	public boolean isIncident(Edge edge) {
		checkNotRemoved();

		return getIncidentEdgeSet().contains(edge);
	}

	@Override
	public boolean isDirected() {
		return directed;
	}

	@Override
	public boolean isPseudo() {
		checkNotRemoved();

		return source.equals(target);
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
			throw new EdgeRemovedException();
		}
	}

	@Override
	public void mergeFrom(Edge edge) {
		getMark().putAll(edge.getMark());
		getVisual().mergeFrom(edge.getVisual());
	}
}
