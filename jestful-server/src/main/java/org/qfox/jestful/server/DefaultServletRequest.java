package org.qfox.jestful.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
 * @date 2016年4月8日 下午4:12:32
 *
 * @since 1.0.0
 */
public class DefaultServletRequest implements Message {
	private final HttpServletRequest request;

	public DefaultServletRequest(HttpServletRequest request) {
		super();
		this.request = request;
	}

	public String[] getHeaders() {
		Enumeration<String> enumeration = request.getHeaderNames();
		if (enumeration == null) {
			return null;
		}
		List<String> names = new ArrayList<String>();
		while (enumeration.hasMoreElements()) {
			names.add(enumeration.nextElement());
		}
		return names.toArray(new String[names.size()]);
	}

	public String getHeader(String name) {
		return request.getHeader(name);
	}

	public void setHeader(String name, String value) {
		throw new UnsupportedOperationException();
	}

	public String[] getHeaders(String name) {
		Enumeration<String> enumeration = request.getHeaders(name);
		if (enumeration == null) {
			return null;
		}
		List<String> values = new ArrayList<String>();
		while (enumeration.hasMoreElements()) {
			values.add(enumeration.nextElement());
		}
		return values.toArray(new String[values.size()]);
	}
	
	public void setHeaders(String name, String[] values) {
		throw new UnsupportedOperationException();
	}

	public InputStream getInputStream() throws IOException {
		return request.getInputStream();
	}

	public OutputStream getOutputStream() throws IOException {
		throw new UnsupportedOperationException();
	}

}
