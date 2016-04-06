package org.qfox.jestful.server.exception;

import java.lang.reflect.Method;
import java.util.List;

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
 * @date 2016年4月6日 下午8:05:21
 *
 * @since 1.0.0
 */
public class DuplicateArgumentException extends IllegalConfigException {
	private static final long serialVersionUID = -7985557920387952095L;

	private final List<Integer> indexes;

	public DuplicateArgumentException(Object controller, Method method, List<Integer> indexes) {
		super("duplicate argument of indexes " + indexes + " defined in method " + method + " of controller " + controller, controller, method);
		this.indexes = indexes;
	}

	public DuplicateArgumentException(String message, Object controller, Method method, List<Integer> indexes) {
		super(message, controller, method);
		this.indexes = indexes;
	}

	public List<Integer> getIndexes() {
		return indexes;
	}

}
