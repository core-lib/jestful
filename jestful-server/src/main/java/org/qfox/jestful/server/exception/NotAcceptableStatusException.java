package org.qfox.jestful.server.exception;

import org.qfox.jestful.commons.MediaType;
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
 * @date 2016年4月9日 下午4:10:00
 *
 * @since 1.0.0
 */
public class NotAcceptableStatusException extends StatusException {
	private static final long serialVersionUID = -3926715913399376714L;

	private final Iterable<MediaType> accepts;
	private final Iterable<MediaType> produces;

	public NotAcceptableStatusException(String uri, String method, Iterable<MediaType> accepts, Iterable<MediaType> produces) {
		super(uri, method, 406, "Not Acceptable");
		this.accepts = accepts;
		this.produces = produces;
	}

	public Iterable<MediaType> getAccepts() {
		return accepts;
	}

	public Iterable<MediaType> getProduces() {
		return produces;
	}

}
