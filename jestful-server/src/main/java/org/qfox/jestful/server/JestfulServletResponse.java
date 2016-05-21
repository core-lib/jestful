package org.qfox.jestful.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.qfox.jestful.core.Response;
import org.qfox.jestful.core.Status;

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
public class JestfulServletResponse extends HttpServletResponseWrapper implements Response {
	private final HttpServletResponse response;

	public JestfulServletResponse(HttpServletResponse response) {
		super(response);
		this.response = response;
	}

	public Status getResponseStatus() throws IOException {
		int code = response.getStatus();
		String reason = Status.SPECIFICATIONS.get(code);
		return new Status(code, reason);
	}

	public void setResponseStatus(Status status) throws IOException {
		if (status.isSuccess()) {
			response.setStatus(status.getCode());
		} else {
			response.sendError(status.getCode(), status.getReason());
		}
	}
	
	public boolean isResponseSuccess() throws IOException {
		Status status = getResponseStatus();
		int code = status.getCode();
		return code >= 200 && code < 300;
	}

	public String[] getHeaderKeys() {
		Collection<String> names = response.getHeaderNames();
		return names != null ? names.toArray(new String[names.size()]) : null;
	}

	public String getResponseHeader(String name) {
		return response.getHeader(name);
	}

	public void setResponseHeader(String name, String value) {
		response.setHeader(name, value);
	}

	public String[] getResponseHeaders(String name) {
		Collection<String> values = response.getHeaders(name);
		return values != null ? values.toArray(new String[values.size()]) : null;
	}

	public void setResponseHeaders(String name, String[] values) {
		for (int i = 0; i < values.length; i++) {
			if (i == 0) {
				response.setHeader(name, values[i]);
			} else {
				response.addHeader(name, values[i]);
			}
		}
	}

	public InputStream getResponseInputStream() throws IOException {
		throw new UnsupportedOperationException();
	}

	public OutputStream getResponseOutputStream() throws IOException {
		return response.getOutputStream();
	}
	
	public void close() throws IOException {
		
	}

}
