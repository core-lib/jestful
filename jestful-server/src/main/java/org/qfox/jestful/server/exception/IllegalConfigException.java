package org.qfox.jestful.server.exception;

import java.lang.reflect.Method;

import org.qfox.jestful.core.exception.JestfulRuntimeException;

/**
 * <p>
 * Description: 配置错误异常
 * </p>
 * 
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 * 
 * @author Payne 646742615@qq.com
 *
 * @date 2016年4月1日 下午6:07:11
 *
 * @since 1.0.0
 */
public class IllegalConfigException extends JestfulRuntimeException {
	private static final long serialVersionUID = -2371085188482186731L;

	private final Object controller;
	private final Method method;

	public IllegalConfigException(String message, Object controller) {
		this(message, controller, null);
	}

	public IllegalConfigException(Throwable cause, Object controller) {
		this(cause, controller, null);
	}

	public IllegalConfigException(String message, Throwable cause, Object controller) {
		this(message, cause, controller, null);
	}

	public IllegalConfigException(String message, Object controller, Method method) {
		super(message);
		this.controller = controller;
		this.method = method;
	}

	public IllegalConfigException(Throwable cause, Object controller, Method method) {
		super(cause);
		this.controller = controller;
		this.method = method;
	}

	public IllegalConfigException(String message, Throwable cause, Object controller, Method method) {
		super(message, cause);
		this.controller = controller;
		this.method = method;
	}

	public Object getController() {
		return controller;
	}

	public Method getMethod() {
		return method;
	}

}
