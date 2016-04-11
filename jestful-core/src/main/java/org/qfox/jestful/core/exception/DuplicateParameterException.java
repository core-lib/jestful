package org.qfox.jestful.core.exception;

import java.lang.reflect.Method;

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
public class DuplicateParameterException extends IllegalConfigException {
	private static final long serialVersionUID = -7985557920387952095L;

	private final int index;

	public DuplicateParameterException(Object controller, Method method, int index) {
		this("duplicate argument at index " + index + " defined in method " + method + " of controller " + controller, controller, method, index);
	}

	public DuplicateParameterException(String message, Object controller, Method method, int index) {
		super(message, controller, method);
		this.index = index;
	}

	public int getIndex() {
		return index;
	}

}
