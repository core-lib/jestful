package org.qfox.jestful.server.exception;

import org.qfox.jestful.core.exception.StatusException;

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
 * @date 2016年3月31日 上午10:50:53
 *
 * @since 1.0.0
 */
public class BadMethodStatusException extends StatusException {
	private static final long serialVersionUID = 6704304796450151398L;

	private final String expect;
	private final String actual;

	public BadMethodStatusException(String expect, String actual) {
		super(405, "Method Not Allowed");
		this.expect = expect;
		this.actual = actual;
	}

	public String getExpect() {
		return expect;
	}

	public String getActual() {
		return actual;
	}

}
