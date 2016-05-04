package org.qfox.jestful.client.exception;

import org.qfox.jestful.core.Status;
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
 * @date 2016年5月4日 下午8:39:56
 *
 * @since 1.0.0
 */
public class UnexpectedStatusException extends JestfulException {
	private static final long serialVersionUID = -3047547153816408719L;

	private final Status status;
	private final String body;

	public UnexpectedStatusException(Status status, String body) {
		super("unexpected response status code : " + status.getCode() + " reason : " + status.getReason() + "\r\n" + body);
		this.status = status;
		this.body = body;
	}

	public Status getStatus() {
		return status;
	}

	public String getBody() {
		return body;
	}

}
