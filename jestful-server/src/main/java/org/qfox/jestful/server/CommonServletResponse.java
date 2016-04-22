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
public class CommonServletResponse extends HttpServletResponseWrapper implements Response {
	private final HttpServletResponse response;

	public CommonServletResponse(HttpServletResponse response) {
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

	public String[] getResponseHeaders() {
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
		response.setHeader(name, null);
		for (String value : values) {
			response.addHeader(name, value);
		}
	}

	public InputStream getResponseInputStream() throws IOException {
		throw new UnsupportedOperationException();
	}

	public OutputStream getResponseOutputStream() throws IOException {
		return response.getOutputStream();
	}

}
