package org.qfox.jestful.server.exception;

import org.qfox.jestful.core.Parameter;
import org.qfox.jestful.core.exception.JestfulException;

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
 * @date 2016年4月8日 上午11:46:47
 *
 * @since 1.0.0
 */
public class UnsupportedParameterException extends JestfulException {
	private static final long serialVersionUID = 4178737794724428116L;

	private final Parameter parameter;

	public UnsupportedParameterException(Parameter parameter) {
		this("unsupported parameter " + parameter, parameter);
	}

	public UnsupportedParameterException(String message, Parameter parameter) {
		super(message);
		this.parameter = parameter;
	}

	public UnsupportedParameterException(String message, Throwable cause, Parameter parameter) {
		super(message, cause);
		this.parameter = parameter;
	}

	public Parameter getParameter() {
		return parameter;
	}

}
