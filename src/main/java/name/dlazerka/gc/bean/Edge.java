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

package name.dlazerka.gc.bean;

/**
 * @author Dzmitry Lazerka www.dlazerka.name
 */
public class Edge {
	private final Vertex tail;
	private final Vertex head;

	public Edge(Vertex tail, Vertex head) {
		this.tail = tail;
		this.head = head;

		tail.addAdjacentEdge(this);
		head.addAdjacentEdge(this);
	}

	public Vertex getTail() {
		return tail;
	}

	public Vertex getHead() {
		return head;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Edge)) return false;

		Edge edge = (Edge) o;

		if (!tail.equals(edge.tail)) return false;
		if (!head.equals(edge.head)) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = tail.hashCode();
		result = 31 * result + head.hashCode();
		return result;
	}

	@Override
	public String toString() {
		return tail + " -> " + head;
	}
}
