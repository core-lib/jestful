package org.qfox.jestful.core.exception;

import org.qfox.jestful.core.Parameter;

import java.lang.reflect.Method;

/**
 * <p>
 * Description:
 * </p>
 * <p>
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 *
 * @author Payne 646742615@qq.com
 * @date 2016年4月12日 上午11:31:29
 * @since 1.0.0
 */
public class AmbiguousParameterException extends IllegalConfigException {
    private static final long serialVersionUID = 6106792803333438394L;

    private final Parameter parameter;

    public AmbiguousParameterException(String message, Object controller, Method method, Parameter parameter) {
        super(message, controller, method);
        this.parameter = parameter;
    }

    public Parameter getParameter() {
        return parameter;
    }

}
