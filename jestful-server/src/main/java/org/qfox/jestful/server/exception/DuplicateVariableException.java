package org.qfox.jestful.server.exception;

import java.lang.reflect.Method;

import org.qfox.jestful.server.Variable;

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
 * @date 2016年4月6日 下午8:49:54
 *
 * @since 1.0.0
 */
public class DuplicateVariableException extends IllegalConfigException {
	private static final long serialVersionUID = 3447667298107703908L;

	private final Variable current;
	private final Variable existed;

	public DuplicateVariableException(Object controller, Method method, Variable current, Variable existed) {
		super("duplicate variables of " + current + " and " + existed + " in method " + method + " of controller " + controller, controller, method);
		this.current = current;
		this.existed = existed;
	}

	public Variable getCurrent() {
		return current;
	}

	public Variable getExisted() {
		return existed;
	}

}
