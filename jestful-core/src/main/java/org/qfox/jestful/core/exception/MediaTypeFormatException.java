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
 * @date 2016年4月9日 上午11:34:40
 *
 * @since 1.0.0
 */
public class MediaTypeFormatException extends JestfulRuntimeException {
	private static final long serialVersionUID = 2006561661819710472L;

	private final String contentType;

	public MediaTypeFormatException(String contentType) {
		super(contentType);
		this.contentType = contentType;
	}

	public MediaTypeFormatException(String message, String contentType) {
		super(message);
		this.contentType = contentType;
	}

	public String getContentType() {
		return contentType;
	}

}
