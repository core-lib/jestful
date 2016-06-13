package org.qfox.jestful.client.gateway;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;

import org.qfox.jestful.core.Action;

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
 * @date 2016年6月13日 下午3:52:43
 *
 * @since 1.0.0
 */
public class ProxyGateway implements Gateway {
	private final String host;
	private final int port;

	public ProxyGateway(String host, int port) {
		super();
		this.host = host;
		this.port = port;
	}

	public boolean isProxy() {
		return true;
	}

	public Proxy toProxy() {
		return new Proxy(Type.HTTP, new InetSocketAddress(host, port));
	}

	public void onConnected(Action action) throws IOException {

	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((host == null) ? 0 : host.hashCode());
		result = prime * result + port;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProxyGateway other = (ProxyGateway) obj;
		if (host == null) {
			if (other.host != null)
				return false;
		} else if (!host.equals(other.host))
			return false;
		if (port != other.port)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "http(s)://" + host + ":" + port;
	}

}
