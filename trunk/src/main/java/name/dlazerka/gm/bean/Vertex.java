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

package name.dlazerka.gm.bean;

import name.dlazerka.gm.util.LinkedSet;

import java.util.Set;

/**
 * @author Dzmitry Lazerka www.dlazerka.name
 */
public class Vertex {
	private final int number;
	private Set<Edge> adjacentEdgeSet = new LinkedSet<Edge>();

	public Vertex(int number) {
		this.number = number;
	}

	public int getNumber() {
		return number;
	}

	public Set<Edge> getAdjacentEdgeSet() {
		return adjacentEdgeSet;
	}

	protected boolean addAdjacentEdge(Edge edge) {
		return adjacentEdgeSet.add(edge);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Vertex)) return false;

		Vertex vertex = (Vertex) o;

		if (number != vertex.number) return false;

		return true;
	}

	@Override
	public int hashCode() {
		return number;
	}

	@Override
	public String toString() {
		return number + "";
	}
}
