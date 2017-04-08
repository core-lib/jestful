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
public class Charsets implements Iterable<Charset>, Cloneable {
	private final Set<Charset> charsets;

	public Charsets(String[] charsets) {
		this.charsets = new TreeSet<Charset>();
		for (String charset : charsets) {
			this.charsets.add(Charset.valueOf(charset));
		}
	}

	public Charsets(Collection<Charset> charsets) {
		this.charsets = new TreeSet<Charset>(charsets);
	}

	public static Charsets valueOf(String accept) {
		Set<Charset> charsets = new TreeSet<Charset>();
		String[] values = accept != null && accept.length() != 0 ? accept.split(",") : new String[0];
		for (String value : values) {
			Charset mediaType = Charset.valueOf(value);
			charsets.add(mediaType);
		}
		return new Charsets(charsets);
	}

	public Charset first() {
		if (isEmpty()) {
			throw new IllegalStateException("empty");
		}
		return iterator().next();
	}

	public Charset last() {
		if (isEmpty()) {
			throw new IllegalStateException("empty");
		}
		Iterator<Charset> iterator = iterator();
		Charset last = iterator.next();
		while (iterator.hasNext()) {
			last = iterator.next();
		}
		return last;
	}

	public int size() {
		return charsets.size();
	}

	public boolean isEmpty() {
		return size() == 0;
	}

	public boolean contains(String name) {
		return contains(Charset.valueOf(name));
	}

	public boolean contains(Charset charset) {
		for (Charset cs : charsets) {
			if (cs.equals(charset)) {
				return true;
			}
		}
		return false;
	}

	public boolean retainAll(Charsets charsets) {
		return this.charsets.retainAll(charsets.charsets);
	}

	public Iterator<Charset> iterator() {
		return charsets.iterator();
	}

	@Override
	public Charsets clone() {
		return new Charsets(charsets);
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		Iterator<Charset> iterator = iterator();
		while (iterator.hasNext()) {
			builder.append(iterator.next().toString());
			if (iterator.hasNext()) {
				builder.append(",");
			}
		}
		return builder.toString();
	}

}
