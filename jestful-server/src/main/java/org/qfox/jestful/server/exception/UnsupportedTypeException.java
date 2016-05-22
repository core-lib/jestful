package org.qfox.jestful.server.exception;

import org.qfox.jestful.core.Accepts;
import org.qfox.jestful.core.MediaType;
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
 * @date 2016年4月9日 下午2:44:19
 *
 * @since 1.0.0
 */
public class UnsupportedTypeException extends StatusException {
	private static final long serialVersionUID = 5552964842892180698L;

	private final MediaType mediaType;
	private final Accepts accepts;

	public UnsupportedTypeException(String uri, String method, MediaType mediaType, Accepts accepts) {
		super(uri, method, 415, "Unsupported Media Type");
		this.mediaType = mediaType;
		this.accepts = accepts;
	}

	public MediaType getMediaType() {
		return mediaType;
	}

	public Accepts getAccepts() {
		return accepts;
	}

}
