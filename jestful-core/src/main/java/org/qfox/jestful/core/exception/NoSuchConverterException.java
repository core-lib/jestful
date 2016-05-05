package org.qfox.jestful.core.exception;

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
 * @date 2016年4月28日 下午6:08:28
 *
 * @since 1.0.0
 */
public class NoSuchConverterException extends JestfulRuntimeException {
	private static final long serialVersionUID = 6803364287861961987L;

	private final Parameter parameter;

	public NoSuchConverterException(Parameter parameter) {
		super("there is no suitable converter to convert parameter " + parameter);
		this.parameter = parameter;
	}

	public Parameter getParameter() {
		return parameter;
	}

}
