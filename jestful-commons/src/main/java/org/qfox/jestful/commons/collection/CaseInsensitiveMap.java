package org.qfox.jestful.commons.collection;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * Description: Case insensitive map
 * </p>
 * 
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 * 
 * @author Payne 646742615@qq.com
 *
 * @date 2016年3月24日 下午9:35:31
 *
 * @since 1.0.0
 */
@SuppressWarnings(value = "all")
public class CaseInsensitiveMap<K extends String, V> implements Map<String, V> {
	private final Set<Entry<String, V>> entries = new LinkedHashSet<Map.Entry<String, V>>();

	public int size() {
		return entries.size();
	}

	public boolean isEmpty() {
		return entries.isEmpty();
	}

	public boolean containsKey(Object key) {
		if (key instanceof String == false) {
			return false;
		}
		for (Entry<String, V> entry : entries) {
			if (key.toString().equalsIgnoreCase(entry.getKey())) {
				return true;
			}
		}
		return false;
	}

	public boolean containsValue(Object value) {
		if (value == null) {
			return false;
		}
		for (Entry<String, V> entry : entries) {
			if (value.equals(entry.getValue())) {
				return true;
			}
		}
		return false;
	}

	public V get(Object key) {
		if (key instanceof String == false) {
			return null;
		}
		for (Entry<String, V> entry : entries) {
			if (key.toString().equalsIgnoreCase(entry.getKey())) {
				return entry.getValue();
			}
		}
		return null;
	}

	public V put(String key, V value) {
		if (key == null || value == null) {
			throw new IllegalArgumentException();
		}
		for (Entry<String, V> entry : entries) {
			if (key.equalsIgnoreCase(entry.getKey())) {
				V old = entry.getValue();
				entry.setValue(value);
				return old;
			}
		}
		Entry<String, V> entry = new SimpleEntry<String, V>(key, value);
		entries.add(entry);
		return null;
	}

	public V remove(Object key) {
		if (key instanceof String == false) {
			return null;
		}
		Iterator<Entry<String, V>> iterator = entries.iterator();
		while (iterator.hasNext()) {
			Entry<String, V> entry = iterator.next();
			if (key.toString().equalsIgnoreCase(entry.getKey())) {
				iterator.remove();
				return entry.getValue();
			}
		}
		return null;
	}

	public void putAll(Map<? extends String, ? extends V> m) {
		Set<? extends String> keys = m.keySet();
		for (String key : keys) {
			V value = m.get(key);
			put(key, value);
		}
	}

	public void clear() {
		entries.clear();
	}

	public Set<String> keySet() {
		Set<String> set = new LinkedHashSet<String>();
		for (Entry<String, V> entry : entries) {
			set.add(entry.getKey());
		}
		return set;
	}

	public Collection<V> values() {
		List<V> collection = new ArrayList<V>();
		for (Entry<String, V> entry : entries) {
			collection.add(entry.getValue());
		}
		return collection;
	}

	public Set<Entry<String, V>> entrySet() {
		return entries;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((entries == null) ? 0 : entries.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CaseInsensitiveMap<?, ?> other = (CaseInsensitiveMap<?, ?>) obj;
		if (entries == null) {
			if (other.entries != null)
				return false;
		} else if (!entries.equals(other.entries))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("{");
		Iterator<Entry<String, V>> iterator = entries.iterator();
		while (iterator.hasNext()) {
			Entry<String, V> entry = iterator.next();
			builder.append(entry.toString());
			if (iterator.hasNext()) {
				builder.append(",");
			}
		}
		builder.append("}");
		return builder.toString();
	}

}
