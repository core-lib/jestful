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
public class StatusException extends JestfulException {
	private static final long serialVersionUID = 6260672121780209011L;

	protected final int code;
	protected final String message;

	public StatusException(int code, String message) {
		super();
		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

}
