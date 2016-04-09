package org.qfox.jestful.server.exception;

import java.util.Set;

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
	private final Set<MediaType> consumes;

	public UnsupportedTypeException(MediaType mediaType, Set<MediaType> consumes) {
		super(415, "Unsupported Media Type");
		this.mediaType = mediaType;
		this.consumes = consumes;
	}

	public MediaType getMediaType() {
		return mediaType;
	}

	public Set<MediaType> getConsumes() {
		return consumes;
	}

}
