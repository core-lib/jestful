package org.qfox.jestful.server.exception;

import java.lang.reflect.Method;
import java.util.List;

import org.qfox.jestful.core.Source;

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
 * @date 2016年4月7日 下午6:58:15
 *
 * @since 1.0.0
 */
public class NonuniqueSourceException extends IllegalConfigException {
	private static final long serialVersionUID = -2444491251889985475L;

	private final int index;
	private final List<Source> sources;

	public NonuniqueSourceException(Object controller, Method method, int index, List<Source> sources) {
		super("parameter at index " + index + " in method " + method + " of controller " + controller + " has multiply sources " + sources, controller, method);
		this.index = index;
		this.sources = sources;
	}

	public int getIndex() {
		return index;
	}

	public List<Source> getSources() {
		return sources;
	}

}
