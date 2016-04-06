package org.qfox.jestful.core.exception;

/**
 * <p>
 * Description: The root exception type of jestful framework
 * </p>
 * 
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 * 
 * @author Payne 646742615@qq.com
 *
 * @date 2016年2月18日 下午5:44:20
 *
 * @since 1.0.0
 */
public class JestfulException extends Exception {
	private static final long serialVersionUID = -6087641550480754318L;

	public JestfulException() {
		super();
	}

	public JestfulException(String message, Throwable cause) {
		super(message, cause);
	}

	public JestfulException(String message) {
		super(message);
	}

	public JestfulException(Throwable cause) {
		super(cause);
	}

}
