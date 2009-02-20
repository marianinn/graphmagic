package name.dlazerka.gc.util;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @author Dzmitry Lazerka
 */
public class LinkedSet2<E> extends LinkedList<E> implements Set<E>, List<E> {
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
