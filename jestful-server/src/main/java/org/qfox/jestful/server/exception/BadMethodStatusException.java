package org.qfox.jestful.server.exception;

import java.util.Set;

import org.qfox.jestful.core.exception.StatusException;
import org.qfox.jestful.server.Mapping;

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

	private final String method;
	private final String uri;
	private final Set<Mapping> mappings;

	public BadMethodStatusException(String method, String uri, Set<Mapping> mappings) {
		super(405, "Method Not Allowed");
		this.uri = uri;
		this.method = method;
		this.mappings = mappings;
	}

	public String getMethod() {
		return method;
	}

	public String getUri() {
		return uri;
	}

	public Set<Mapping> getMappings() {
		return mappings;
	}

}
