package org.qfox.jestful.commons.collection;

import java.util.Enumeration;
import java.util.Iterator;

/**
 * <p>
 * Description: Iterator wrapper, close it's {@link Iterator#remove()} method
 * </p>
 * 
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 * 
 * @author Payne 646742615@qq.com
 *
 * @date 2016年4月8日 下午4:33:13
 *
 * @since 1.0.0
 */
public class Enumerator<T> implements Enumeration<T> {
	private final Iterator<T> iterator;

	public Enumerator(Iterator<T> iterator) {
		super();
		this.iterator = iterator;
	}

	public Enumerator(Iterable<T> iterable) {
		super();
		this.iterator = iterable.iterator();
	}

	public boolean hasMoreElements() {
		return iterator.hasNext();
	}

	public T nextElement() {
		return iterator.next();
	}

}
