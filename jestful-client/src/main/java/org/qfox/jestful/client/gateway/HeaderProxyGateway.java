package org.qfox.jestful.client.gateway;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.qfox.jestful.commons.collection.CaseInsensitiveMap;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Request;

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
 * @date 2016年6月13日 下午4:07:35
 *
 * @since 1.0.0
 */
public class HeaderProxyGateway extends ProxyGateway {
	private final Map<String, String> header;

	public HeaderProxyGateway(String host, int port, String name, String value) {
		this(host, port, null);
		this.header.put(name, value);
	}

	public HeaderProxyGateway(String host, int port, Map<String, String> header) {
		super(host, port);
		this.header = new CaseInsensitiveMap<String, String>(header != null ? header : new HashMap<String, String>());
	}

	@Override
	public void onConnected(Action action) throws IOException {
		Request request = action.getRequest();
		for (Entry<String, String> entry : header.entrySet()) {
			request.setRequestHeader(entry.getKey(), entry.getValue());
		}
	}

	public Map<String, String> getHeader() {
		return header;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((header == null) ? 0 : header.hashCode());
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
		HeaderProxyGateway other = (HeaderProxyGateway) obj;
		if (header == null) {
			if (other.header != null)
				return false;
		} else if (!header.equals(other.header))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(super.toString());
		builder.append("\r\n");
		for (Entry<String, String> entry : header.entrySet()) {
			builder.append(entry.getKey() + ": " + entry.getValue());
			builder.append("\r\n");
		}
		return builder.toString();
	}

}
