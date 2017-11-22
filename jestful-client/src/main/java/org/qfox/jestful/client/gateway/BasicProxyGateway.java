package org.qfox.jestful.client.gateway;

import org.qfox.jestful.commons.Base64;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Request;

import java.io.IOException;

/**
 * <p>
 * Description:
 * </p>
 * <p>
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 *
 * @author Payne 646742615@qq.com
 * @date 2016年6月13日 下午5:23:23
 * @since 1.0.0
 */
public class BasicProxyGateway extends ProxyGateway {
	private final String username;
	private final String password;

	public BasicProxyGateway(String host, int port, String username, String password) {
		super(host, port);
		this.username = username;
		this.password = password;
	}

	@Override
	public void onConnected(Action action) throws IOException {
		Request request = action.getRequest();
		String authorization = "Basic " + Base64.encode(username + ":" + password);
		request.setRequestHeader("Proxy-Authorization", authorization);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		BasicProxyGateway other = (BasicProxyGateway) obj;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(super.toString());
		builder.append("\r\n");
		String authorization = "Basic " + Base64.encode(username + ":" + password);
		builder.append("Proxy-Authorization: ").append(authorization);
		builder.append("\r\n");
		return builder.toString();
	}

}
