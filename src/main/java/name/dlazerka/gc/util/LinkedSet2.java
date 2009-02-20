package name.dlazerka.gc.util;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @author Dzmitry Lazerka
 */
public class LinkedSet2<T> extends LinkedList<T> implements Set<T>, List<T> {
	@Override
	public void addFirst(T t) {
		if (!contains(t)) {
			super.addFirst(t);
		}
	}

	@Override
	public void addLast(T t) {
		if (!contains(t)) {
			super.addLast(t);
		}
	}

	@Override
	public boolean add(T t) {
		if (!contains(t)) {
			return super.add(t);
		}
		return false;
	}

	@Override
	public T set(int index, T element) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void add(int index, T element) {
		throw new UnsupportedOperationException();
	}
}
