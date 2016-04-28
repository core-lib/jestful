package org.qfox.jestful.client.exception;

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
 * @date 2016年4月28日 下午7:15:09
 *
 * @since 1.0.0
 */
public class IllegalQueryException extends JestfulException {
	private static final long serialVersionUID = -7318094108970824890L;

	private final String query;

	public IllegalQueryException(String message, String query) {
		super(message);
		this.query = query;
	}

	public String getQuery() {
		return query;
	}

}
