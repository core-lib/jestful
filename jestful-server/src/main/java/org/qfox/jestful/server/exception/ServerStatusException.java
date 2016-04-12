package org.qfox.jestful.server.exception;

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
 * @date 2016年4月12日 下午9:43:36
 *
 * @since 1.0.0
 */
public class ServerStatusException extends StatusException {
	private static final long serialVersionUID = -8338726210762952801L;

	public ServerStatusException(String message) {
		super(500, message);
	}

}
