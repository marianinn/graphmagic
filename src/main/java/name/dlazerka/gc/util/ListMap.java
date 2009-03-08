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
