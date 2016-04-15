package org.qfox.jestful.commons.collection;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * <p>
 * Description: Case insensitive set
 * </p>
 * 
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 * 
 * @author Payne 646742615@qq.com
 *
 * @date 2016年4月15日 下午4:02:45
 *
 * @since 1.0.0
 */
@SuppressWarnings(value = "all")
public class CaseInsensitiveSet<E extends String> implements Set<String> {
	private final Set<String> elements = new LinkedHashSet<String>();

	public CaseInsensitiveSet() {
		super();
	}

	public CaseInsensitiveSet(Collection<String> collection) {
		this.addAll(collection);
	}

	public int size() {
		return elements.size();
	}

	public boolean isEmpty() {
		return elements.isEmpty();
	}

	public boolean contains(Object o) {
		if (o instanceof String) {
			String string = (String) o;
			Iterator<String> iterator = iterator();
			while (iterator.hasNext()) {
				String element = iterator.next();
				if (string.equalsIgnoreCase(element)) {
					return true;
				}
			}
			return false;
		} else {
			return false;
		}
	}

	public Iterator<String> iterator() {
		return elements.iterator();
	}

	public Object[] toArray() {
		return elements.toArray();
	}

	public <T> T[] toArray(T[] a) {
		return elements.toArray(a);
	}

	public boolean add(String e) {
		if (e == null) {
			throw new IllegalArgumentException("add null element");
		}
		if (contains(e)) {
			return false;
		} else {
			elements.add(e);
			return true;
		}
	}

	public boolean remove(Object o) {
		if (o instanceof String) {
			String string = (String) o;
			Iterator<String> iterator = iterator();
			while (iterator.hasNext()) {
				String element = iterator.next();
				if (string.equalsIgnoreCase(element)) {
					iterator.remove();
					return true;
				}
			}
			return false;
		} else {
			return false;
		}
	}

	public boolean containsAll(Collection<?> c) {
		for (Object element : c) {
			if (contains(element) == false) {
				return false;
			}
		}
		return true;
	}

	public boolean addAll(Collection<? extends String> c) {
		boolean changed = false;
		for (String element : c) {
			changed = changed || add(element);
		}
		return changed;
	}

	public boolean retainAll(Collection<?> c) {
		CaseInsensitiveSet<String> set = new CaseInsensitiveSet<String>();
		for (Object o : c) {
			if (o instanceof String) {
				set.add((String) o);
			}
		}
		boolean changed = false;
		Iterator<String> iterator = iterator();
		while (iterator.hasNext()) {
			String element = iterator.next();
			if (set.contains(element) == false) {
				iterator.remove();
				changed = true;
			}
		}
		return changed;
	}

	public boolean removeAll(Collection<?> c) {
		CaseInsensitiveSet<String> set = new CaseInsensitiveSet<String>();
		for (Object o : c) {
			if (o instanceof String) {
				set.add((String) o);
			}
		}
		boolean changed = false;
		Iterator<String> iterator = iterator();
		while (iterator.hasNext()) {
			String element = iterator.next();
			if (set.contains(element)) {
				iterator.remove();
				changed = true;
			}
		}
		return changed;
	}

	public void clear() {
		elements.clear();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((elements == null) ? 0 : elements.hashCode());
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
		CaseInsensitiveSet other = (CaseInsensitiveSet) obj;
		if (elements == null) {
			if (other.elements != null)
				return false;
		} else if (!elements.equals(other.elements))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return elements.toString();
	}

}
