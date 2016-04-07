package org.qfox.jestful.server.exception;

import org.qfox.jestful.server.Mapping;

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
 * @date 2016年4月1日 下午6:06:17
 *
 * @since 1.0.0
 */
public class DuplicateMappingException extends IllegalConfigException {
	private static final long serialVersionUID = 2712160694691373734L;

	private final Mapping current;
	private final Mapping existed;

	public DuplicateMappingException(Mapping current, Mapping existed) {
		this(null, current, existed);
	}

	public DuplicateMappingException(Throwable cause, Mapping current, Mapping existed) {
		super("duplicate mapping " + current.toLogString() + " with " + existed.toLogString(), current.getOperation().getResource().getController(), current.getOperation().getMethod());
		this.current = current;
		this.existed = existed;
	}

	public Mapping getCurrent() {
		return current;
	}

	public Mapping getExisted() {
		return existed;
	}

}
