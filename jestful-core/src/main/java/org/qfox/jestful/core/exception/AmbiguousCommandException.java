package org.qfox.jestful.core.exception;

import java.lang.reflect.Method;

import org.qfox.jestful.core.Operation;

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
 * @date 2016年4月12日 上午11:31:29
 *
 * @since 1.0.0
 */
public class AmbiguousCommandException extends IllegalConfigException {
	private static final long serialVersionUID = 6106792803333438394L;

	private final Operation operation;

	public AmbiguousCommandException(String message, Object controller, Method method, Operation operation) {
		super(message, controller, method);
		this.operation = operation;
	}

	public Operation getOperation() {
		return operation;
	}

}
