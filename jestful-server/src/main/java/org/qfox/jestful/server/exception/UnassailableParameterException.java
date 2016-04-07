package org.qfox.jestful.server.exception;

import java.lang.reflect.Method;
import java.util.Collection;

import org.qfox.jestful.core.Parameter;

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
 * @date 2016年4月7日 下午8:55:11
 *
 * @since 1.0.0
 */
public class UnassailableParameterException extends IllegalConfigException {
	private static final long serialVersionUID = 3833290490897159462L;

	private final Collection<Parameter> parameters;

	public UnassailableParameterException(Object controller, Method method, Collection<Parameter> parameters) {
		super("unassailable parameter " + parameters + " in method " + method + " of controller " + controller, controller, method);
		this.parameters = parameters;
	}

	public Collection<Parameter> getParameters() {
		return parameters;
	}

}
