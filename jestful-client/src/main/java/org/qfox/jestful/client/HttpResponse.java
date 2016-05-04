package org.qfox.jestful.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.List;

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
 * @date 2016年4月27日 下午9:30:33
 *
 * @since 1.0.0
 */
public class HttpResponse implements Response {
	private final HttpURLConnection httpURLConnection;

	public HttpResponse(HttpURLConnection httpURLConnection) {
		super();
		this.httpURLConnection = httpURLConnection;
	}

	public String[] getHeaderKeys() {
		return httpURLConnection.getHeaderFields().keySet().toArray(new String[0]);
	}

	public String getResponseHeader(String name) {
		return httpURLConnection.getHeaderField(name);
	}

	public void setResponseHeader(String name, String value) {
		throw new UnsupportedOperationException();
	}

	public String[] getResponseHeaders(String name) {
		List<String> values = httpURLConnection.getHeaderFields().get(name);
		return values != null ? values.toArray(new String[values.size()]) : null;
	}

	public void setResponseHeaders(String name, String[] values) {
		throw new UnsupportedOperationException();
	}

	public InputStream getResponseInputStream() throws IOException {
		if (isResponseSuccess()) {
			return httpURLConnection.getInputStream();
		} else {
			return httpURLConnection.getErrorStream();
		}
	}

	public OutputStream getResponseOutputStream() throws IOException {
		throw new UnsupportedOperationException();
	}

	public Status getResponseStatus() throws IOException {
		int code = httpURLConnection.getResponseCode();
		String reason = httpURLConnection.getResponseMessage();
		return new Status(code, reason);
	}

	public void setResponseStatus(Status status) throws IOException {
		throw new UnsupportedOperationException();
	}

	public boolean isResponseSuccess() throws IOException {
		Status status = getResponseStatus();
		int code = status.getCode();
		return code >= 200 && code < 300;
	}

}
