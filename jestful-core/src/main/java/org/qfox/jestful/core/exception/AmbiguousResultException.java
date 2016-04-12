package org.qfox.jestful.core.exception;

import java.lang.reflect.Method;

import org.qfox.jestful.core.Result;

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
public class AmbiguousResultException extends IllegalConfigException {
	private static final long serialVersionUID = 6106792803333438394L;

	private final Result result;

	public AmbiguousResultException(String message, Object controller, Method method, Result result) {
		super(message, controller, method);
		this.result = result;
	}

	public Result getResult() {
		return result;
	}

}
