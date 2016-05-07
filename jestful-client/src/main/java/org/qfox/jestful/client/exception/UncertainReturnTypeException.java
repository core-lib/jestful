package org.qfox.jestful.client.exception;

import java.lang.reflect.Type;

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
 * @date 2016年5月7日 下午3:54:29
 *
 * @since 1.0.0
 */
public class UncertainReturnTypeException extends JestfulException {
	private static final long serialVersionUID = 376421971058848789L;

	private final Type type;

	public UncertainReturnTypeException(Type type) {
		super();
		this.type = type;
	}

	public Type getType() {
		return type;
	}

}
