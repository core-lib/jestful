package org.qfox.jestful.server.exception;

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
 * @date 2016年4月6日 下午8:15:17
 *
 * @since 1.0.0
 */
public class UndefinedVariableException extends IllegalConfigException {
	private static final long serialVersionUID = 6155312748066193777L;

	private final String name;
	private final int index;

	public UndefinedVariableException(Object controller, Method method, String name) {
		super("undefined variable named " + name + " in method " + method + " of controller " + controller, controller, method);
		this.name = name;
		this.index = -1;
	}

	public UndefinedVariableException(Object controller, Method method, int index) {
		super("undefined variable at index " + index + " in method " + method + " of controller " + controller, controller, method);
		this.name = null;
		this.index = index;
	}

	public String getName() {
		return name;
	}

	public int getIndex() {
		return index;
	}

}
