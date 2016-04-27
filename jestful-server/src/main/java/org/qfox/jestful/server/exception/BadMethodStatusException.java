package org.qfox.jestful.server.exception;

import java.util.Collection;

import org.qfox.jestful.core.Mapping;
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
 * @date 2016年3月31日 上午10:50:53
 *
 * @since 1.0.0
 */
public class BadMethodStatusException extends StatusException {
	private static final long serialVersionUID = 6704304796450151398L;

	private final Collection<Mapping> mappings;

	public BadMethodStatusException(String uri, String method, Collection<Mapping> mappings) {
		super(uri, method, 405, "Method Not Allowed");
		this.mappings = mappings;
	}

	public Collection<Mapping> getMappings() {
		return mappings;
	}

}
