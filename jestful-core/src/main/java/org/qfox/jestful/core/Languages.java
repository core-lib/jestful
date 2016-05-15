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
public class Languages implements Iterable<Language>, Cloneable {
	private final Set<Language> languages;

	public Languages(String[] languages) {
		this.languages = new TreeSet<Language>();
		for (String language : languages) {
			this.languages.add(Language.valueOf(language));
		}
	}

	public Languages(Collection<Language> languages) {
		this.languages = new TreeSet<Language>(languages);
	}

	public static Languages valueOf(String accept) {
		Set<Language> languages = new TreeSet<Language>();
		String[] values = accept != null && accept.isEmpty() == false ? accept.split(",") : new String[0];
		for (String value : values) {
			Language mediaType = Language.valueOf(value);
			languages.add(mediaType);
		}
		return new Languages(languages);
	}

	public Language first() {
		if (isEmpty()) {
			throw new IllegalStateException("empty");
		}
		return iterator().next();
	}

	public Language last() {
		if (isEmpty()) {
			throw new IllegalStateException("empty");
		}
		Iterator<Language> iterator = iterator();
		Language last = iterator.next();
		while (iterator.hasNext()) {
			last = iterator.next();
		}
		return last;
	}

	public int size() {
		return languages.size();
	}

	public boolean isEmpty() {
		return size() == 0;
	}

	public boolean contains(String name) {
		return contains(Language.valueOf(name));
	}

	public boolean contains(Language language) {
		for (Language cs : languages) {
			if (cs.equals(language)) {
				return true;
			}
		}
		return false;
	}

	public boolean retainAll(Languages languages) {
		return this.languages.retainAll(languages.languages);
	}

	public Iterator<Language> iterator() {
		return languages.iterator();
	}

	@Override
	public Object clone() {
		return new Languages(languages);
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		Iterator<Language> iterator = iterator();
		while (iterator.hasNext()) {
			builder.append(iterator.next().toString());
			if (iterator.hasNext()) {
				builder.append(",");
			}
		}
		return builder.toString();
	}

}
