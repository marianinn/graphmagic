package name.dlazerka.gc.util;

import java.util.*;

/**
 * @author Dzmitry Lazerka
 */
public class LinkedSet<T> extends AbstractSet<T> implements Set<T>, List<T> {
	private LinkedList<T> elements = new LinkedList<T>();

	@Override
	public Iterator<T> iterator() {
		return elements.iterator();
	}

	@Override
	public int size() {
		return elements.size();
	}

	public boolean addAll(int index, Collection<? extends T> c) {
		throw new UnsupportedOperationException();
	}

	public T get(int index) {
		throw new UnsupportedOperationException();
	}

	public T set(int index, T element) {
		throw new UnsupportedOperationException();
	}

	public void add(int index, T element) {
		throw new UnsupportedOperationException();
	}

	public T remove(int index) {
		return elements.remove(index);
	}

	public int indexOf(Object o) {
		return elements.indexOf(o);
	}

	public int lastIndexOf(Object o) {
		return elements.lastIndexOf(o);
	}

	public ListIterator<T> listIterator() {
		return elements.listIterator();
	}

	public ListIterator<T> listIterator(int index) {
		return elements.listIterator(index);
	}

	public List<T> subList(int fromIndex, int toIndex) {
		return elements.subList(fromIndex, toIndex);
	}
}
