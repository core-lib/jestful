package org.qfox.jestful.client.exception;

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
 * @date 2016年4月28日 下午7:17:13
 *
 * @since 1.0.0
 */
public class IllegalCookieException extends JestfulException {
	private static final long serialVersionUID = 3792838453958003723L;

	private final String cookie;

	public IllegalCookieException(String message, String cookie) {
		super(message);
		this.cookie = cookie;
	}

	public String getCookie() {
		return cookie;
	}

}
