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
public class Charsets implements Iterable<Charset> {
	private final Set<Charset> charsets;

	public Charsets(Collection<Charset> charsets) {
		this.charsets = new TreeSet<Charset>(charsets);
	}

	public Iterator<Charset> iterator() {
		return charsets.iterator();
	}

}
