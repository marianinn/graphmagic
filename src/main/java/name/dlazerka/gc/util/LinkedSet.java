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

package name.dlazerka.gc.util;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * A simple implementation of {@link Set} and {@link List} interfaces
 * implemented by using {@link LinkedList} class.
 * Thus, adding to the {@link LinkedSet} is asymptotically inefficient -- O(N),
 * do not use it for large collections (more than 50 elements).
 *
 * @author Dzmitry Lazerka www.dlazerka.name
 */
public class LinkedSet<E> extends LinkedList<E> implements Set<E>, List<E> {
	@Override
	public void addFirst(E e) {
		if (!contains(e)) {
			super.addFirst(e);
		}
	}

	@Override
	public void addLast(E e) {
		if (!contains(e)) {
			super.addLast(e);
		}
	}

	@Override
	public boolean add(E e) {
		if (!contains(e)) {
			return super.add(e);
		}
		return false;
	}

	@Override
	public E set(int index, E element) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void add(int index, E element) {
		throw new UnsupportedOperationException();
	}
}
