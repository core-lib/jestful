package org.qfox.jestful.core;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

import org.qfox.jestful.commons.MediaType;

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
 * @date 2016年4月30日 下午5:31:46
 *
 * @since 1.0.0
 */
public class Accepts implements Iterable<MediaType> {
	private final Set<MediaType> mediaTypes;

	public Accepts(Set<MediaType> mediaTypes) {
		super();
		this.mediaTypes = Collections.unmodifiableSet(mediaTypes);
	}

	public Iterator<MediaType> iterator() {
		return mediaTypes.iterator();
	}

}
