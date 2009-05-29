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

package name.dlazerka.gm.dijkstra;

import name.dlazerka.gm.Vertex;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

/**
 * @author Dzmitry Lazerka www.dlazerka.name
 */
public class VertexQueue implements DijkstraQueue {
	private final Comparator<Vertex> comparator;
	private List<Vertex> list = new ArrayList<Vertex>();

	public VertexQueue(Comparator<Vertex> comparator) {
		this.comparator = comparator;
	}

	public void addAll(Set<Vertex> vertexCollection) {
		for (Vertex vertex : vertexCollection) {
			list.add(vertex);
		}
	}

	@Override
	public void heapify() {
		//noinspection ForLoopReplaceableByForEach
		for (int i = 0; i < list.size(); i++) {
			Vertex movingVertex = list.get(i);
			update(movingVertex);
		}
	}

	@Override
	public Vertex extractMin() {
		Vertex vertex = list.remove(0);
		return vertex;
	}

	@Override
	public void update(Vertex v) {
		int i = list.indexOf(v);

		if (i == 0) return;

		Vertex comparedVertex = list.get(i - 1);
		while (i > 0 && comparator.compare(v, comparedVertex) < 0) {
			list.set(i, comparedVertex);
			list.set(i - 1, v);
			i--;
			comparedVertex = list.get(i - 1);
		}
	}

	@Override
	public boolean isEmpty() {
		return list.isEmpty();
	}
}
