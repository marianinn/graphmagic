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

package name.dlazerka.gm.util;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

/**
 * @author Dzmitry Lazerka www.dlazerka.name
 */
public class ListMap<K, V> extends AbstractMap<K, V> implements Map<K, V> {
	LinkedSet<Map.Entry<K, V>> entrySet = new LinkedSet<Map.Entry<K, V>>();

	public ListMap(Entry<K, V>... initialEntries) {
		entrySet.addAll(Arrays.asList(initialEntries));
	}

	/**
	 * Creates map with one key-value pair.
	 *
	 * @param key   key
	 * @param value value
	 */
	public ListMap(K key, V value) {
		put(key, value);
	}

	@Override
	public Set<Map.Entry<K, V>> entrySet() {
		return entrySet;
	}

	@Override
	public V put(K key, V value) {
		V result;
		Entry<K, V> entry = new Entry<K, V>(key, value);

		int index = entrySet.indexOf(entry);
		if (index != -1) {
			result = entrySet.get(index).getValue();
			entrySet.get(index).setValue(value);
		} else {
			entrySet.add(entry);
			result = null;
		}

		return result;
	}

	protected class Entry<K, V> implements Map.Entry<K, V> {
		private K key;
		private V value;

		public Entry(K key, V value) {
			this.key = key;
			this.value = value;
		}

		public K getKey() {
			return key;
		}

		public V getValue() {
			return value;
		}

		public V setValue(V value) {
			V oldValue = this.value;
			this.value = value;
			return oldValue;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (!(o instanceof Entry)) return false;

			Entry entry = (Entry) o;

			if (key != null ? !key.equals(entry.key) : entry.key != null) return false;

			return true;
		}

		@Override
		public int hashCode() {
			return key != null ? key.hashCode() : 0;
		}
	}
}
