package org.qfox.jestful.core.exception;

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
 * @date 2016年3月31日 上午10:16:55
 *
 * @since 1.0.0
 */
public class StatusException extends JestfulIOException {
	private static final long serialVersionUID = 6260672121780209011L;

	protected final String uri;
	protected final String method;
	protected final int status;
	protected final String message;

	public StatusException(String uri, String method, int status, String message) {
		super();
		this.uri = uri;
		this.method = method;
		this.status = status;
		this.message = message;
	}

	public String getUri() {
		return uri;
	}

	public String getMethod() {
		return method;
	}

	public int getStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}

}
