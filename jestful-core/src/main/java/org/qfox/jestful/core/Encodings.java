package org.qfox.jestful.core;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 * 
 * @author Payne 646742615@qq.com
 *
 * @date 2016年5月15日 上午11:24:36
 *
 * @since 1.0.0
 */
public class Encodings implements Iterable<Encoding>, Cloneable {
	private final Set<Encoding> encodings;

	public Encodings(String[] encodings) {
		this.encodings = new TreeSet<Encoding>();
		for (String encoding : encodings) {
			this.encodings.add(Encoding.valueOf(encoding));
		}
	}

	public Encodings(Collection<Encoding> encodings) {
		this.encodings = new TreeSet<Encoding>(encodings);
	}

	public static Encodings valueOf(String accept) {
		Set<Encoding> encodings = new TreeSet<Encoding>();
		String[] values = accept != null && accept.length() == 0 == false ? accept.split(",") : new String[0];
		for (String value : values) {
			Encoding mediaType = Encoding.valueOf(value);
			encodings.add(mediaType);
		}
		return new Encodings(encodings);
	}

	public Encoding first() {
		if (isEmpty()) {
			throw new IllegalStateException("empty");
		}
		return iterator().next();
	}

	public Encoding last() {
		if (isEmpty()) {
			throw new IllegalStateException("empty");
		}
		Iterator<Encoding> iterator = iterator();
		Encoding last = iterator.next();
		while (iterator.hasNext()) {
			last = iterator.next();
		}
		return last;
	}

	public int size() {
		return encodings.size();
	}

	public boolean isEmpty() {
		return size() == 0;
	}

	public boolean contains(String name) {
		return contains(Encoding.valueOf(name));
	}

	public boolean contains(Encoding encoding) {
		for (Encoding cs : encodings) {
			if (cs.equals(encoding)) {
				return true;
			}
		}
		return false;
	}

	public boolean retainAll(Encodings encodings) {
		return this.encodings.retainAll(encodings.encodings);
	}

	public Iterator<Encoding> iterator() {
		return encodings.iterator();
	}

	@Override
	public Encodings clone() {
		return new Encodings(encodings);
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		Iterator<Encoding> iterator = iterator();
		while (iterator.hasNext()) {
			builder.append(iterator.next().toString());
			if (iterator.hasNext()) {
				builder.append(",");
			}
		}
		return builder.toString();
	}

}
