package org.qfox.jestful.client.exception;

import org.qfox.jestful.core.Accepts;
import org.qfox.jestful.core.MediaType;
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
 * @date 2016年5月4日 下午9:45:31
 *
 * @since 1.0.0
 */
public class UnexpectedTypeException extends JestfulException {
	private static final long serialVersionUID = 4950971763482628055L;

	private final MediaType mediaType;
	private final Accepts accepts;

	public UnexpectedTypeException(MediaType mediaType, Accepts accepts) {
		super("expect content type : " + accepts.toString() + " but got : " + mediaType);
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
