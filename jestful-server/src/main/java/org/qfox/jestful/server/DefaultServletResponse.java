package org.qfox.jestful.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;

import javax.servlet.http.HttpServletResponse;

import org.qfox.jestful.core.Message;

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
 * @date 2016年4月8日 下午4:14:37
 *
 * @since 1.0.0
 */
public class DefaultServletResponse implements Message {
	private final HttpServletResponse response;

	public DefaultServletResponse(HttpServletResponse response) {
		super();
		this.response = response;
	}

	public String[] getHeaders() {
		Collection<String> names = response.getHeaderNames();
		return names != null ? names.toArray(new String[names.size()]) : null;
	}

	public String getHeader(String name) {
		return response.getHeader(name);
	}

	public void setHeader(String name, String value) {
		response.setHeader(name, value);
	}

	public String[] getHeaders(String name) {
		Collection<String> values = response.getHeaders(name);
		return values != null ? values.toArray(new String[values.size()]) : null;
	}

	public void setHeaders(String name, String[] values) {
		response.setHeader(name, null);
		for (String value : values) {
			response.addHeader(name, value);
		}
	}

	public InputStream getInputStream() throws IOException {
		throw new UnsupportedOperationException();
	}

	public OutputStream getOutputStream() throws IOException {
		return response.getOutputStream();
	}

}
