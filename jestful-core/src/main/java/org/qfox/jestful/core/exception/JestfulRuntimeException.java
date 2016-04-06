package org.qfox.jestful.core.exception;

/**
 * <p>
 * Description: The root runtime exception type of jestful framework
 * </p>
 * 
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 * 
 * @author Payne 646742615@qq.com
 *
 * @date 2016年2月18日 下午5:45:32
 *
 * @since 1.0.0
 */
public class JestfulRuntimeException extends RuntimeException {
	private static final long serialVersionUID = 6452550642531505993L;

	public JestfulRuntimeException() {
		super();
	}

	public JestfulRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public JestfulRuntimeException(String message) {
		super(message);
	}

	public JestfulRuntimeException(Throwable cause) {
		super(cause);
	}

}
