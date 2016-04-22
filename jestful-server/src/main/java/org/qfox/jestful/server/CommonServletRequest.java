package org.qfox.jestful.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

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
 * @date 2016年4月8日 下午4:12:32
 *
 * @since 1.0.0
 */
public class CommonServletRequest extends HttpServletRequestWrapper implements Request {
	private final HttpServletRequest request;

	public CommonServletRequest(HttpServletRequest request) {
		super(request);
		this.request = request;
	}

	public String[] getRequestHeaders() {
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

	public String getRequestHeader(String name) {
		return request.getHeader(name);
	}

	public void setRequestHeader(String name, String value) {
		throw new UnsupportedOperationException();
	}

	public String[] getRequestHeaders(String name) {
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

	public void setRequestHeaders(String name, String[] values) {
		throw new UnsupportedOperationException();
	}

	public InputStream getRequestInputStream() throws IOException {
		return request.getInputStream();
	}

	public OutputStream getRequestOutputStream() throws IOException {
		throw new UnsupportedOperationException();
	}

}
