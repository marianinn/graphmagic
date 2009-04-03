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

import java.util.Set;

/**
 * @author Dzmitry Lazerka www.dlazerka.name
 */
public class BasicVertex implements Vertex {
	private final LabeledGraph graph;
	private final int number;
	private final Visual visual;

	/**
	 * Protected constructor. To obtain new instance see {@link Graph#createVertex()}.
	 *
	 * @param graph
	 *@param number number that indentifies this vertex in its graph.  @see Graph#createVertex()
	 */
	protected BasicVertex(LabeledGraph graph, int number) {
		this.graph = graph;
		this.number = number;
		this.visual = new Visual();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		BasicVertex vertex = (BasicVertex) o;

		return number == vertex.number;
	}

	@Override
	public int hashCode() {
		return number;
	}

	@Override
	public String toString() {
		return number + "";
	}

	public int getNumber() {
		return number;
	}

	@Override
	public Visual getVisual() {
		return visual;
	}

	@Override
	public Set<Edge> getAdjacentEdges() {
		LinkedSet<Edge> adjacentEdgeSet = new LinkedSet<Edge>();

		for (Edge edge : graph.getEdgeSet()) {
			if (this.equals(edge.getHead()) ||
			    this.equals(edge.getTail()))
			{
				adjacentEdgeSet.add(edge);
			}
		}

		return adjacentEdgeSet;
	}
}
