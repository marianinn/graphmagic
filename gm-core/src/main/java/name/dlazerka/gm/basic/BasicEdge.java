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
import name.dlazerka.gm.Vertex;
import name.dlazerka.gm.Visual;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Dzmitry Lazerka www.dlazerka.name
 */
public class BasicEdge implements Edge {
	private static final Logger logger = LoggerFactory.getLogger(BasicEdge.class); 

	private final Vertex tail;
	private final Vertex head;
	private final Visual visual;

	protected BasicEdge(Vertex tail, Vertex head) {
		this.tail = tail;
		this.head = head;
		visual = new Visual();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof BasicEdge)) return false;

		BasicEdge edge = (BasicEdge) o;

		return tail.equals(edge.tail) && head.equals(edge.head);
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

	public Vertex getTail() {
		return tail;
	}

	public Vertex getHead() {
		return head;
	}

	public Visual getVisual() {
		return visual;
	}

}
