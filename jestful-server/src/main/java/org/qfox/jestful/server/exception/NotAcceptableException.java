package org.qfox.jestful.server.exception;

import java.util.Set;

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
public class NotAcceptableException extends StatusException {
	private static final long serialVersionUID = -3926715913399376714L;

	private final Set<MediaType> accepts;
	private final Set<MediaType> produces;

	public NotAcceptableException(Set<MediaType> accepts, Set<MediaType> produces) {
		super(406, "Not Acceptable");
		this.accepts = accepts;
		this.produces = produces;
	}

	public Set<MediaType> getAccepts() {
		return accepts;
	}

	public Set<MediaType> getProduces() {
		return produces;
	}

}
