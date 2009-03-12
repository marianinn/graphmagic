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

import java.util.AbstractMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Dzmitry Lazerka www.dlazerka.name
 */
public class ListMap<K, V> extends AbstractMap<K, V> implements Map<K, V> {
	LinkedSet<Map.Entry<K, V>> entrySet = new LinkedSet<Entry<K,V>>();

	@Override
	public Set<Entry<K, V>> entrySet() {
		return entrySet;
	}

	@Override
	public V put(K key, V value) {
		V result;
		Map.Entry<K, V> entry = new SimpleEntry<K, V>(key, value);

		int index = entrySet.indexOf(entry);
		if (index != -1) {
			result = entrySet.get(index).getValue();
			entrySet.get(index).setValue(value);
		}
		else {
			entrySet.add(entry);
			result = null;
		}
		
		return result;
	}
}
