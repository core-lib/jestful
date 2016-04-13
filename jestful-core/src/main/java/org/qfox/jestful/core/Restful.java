package org.qfox.jestful.core;

import org.qfox.jestful.core.annotation.Command;

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
 * @date 2016年4月13日 上午11:56:47
 *
 * @since 1.0.0
 */
public class Restful {
	private final String method;
	private final boolean acceptBody;
	private final boolean returnBody;
	private final boolean idempotent;

	public Restful(Command command) {
		this.method = command.name();
		this.acceptBody = command.acceptBody();
		this.returnBody = command.returnBody();
		this.idempotent = command.idempotent();
	}

	public Restful(String method, boolean acceptBody, boolean returnBody, boolean idempotent) {
		super();
		this.method = method;
		this.acceptBody = acceptBody;
		this.returnBody = returnBody;
		this.idempotent = idempotent;
	}

	public String getMethod() {
		return method;
	}

	public boolean isAcceptBody() {
		return acceptBody;
	}

	public boolean isReturnBody() {
		return returnBody;
	}

	public boolean isIdempotent() {
		return idempotent;
	}

}
