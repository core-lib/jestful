package org.qfox.jestful.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.List;

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
 * @date 2016年4月27日 下午9:17:47
 *
 * @since 1.0.0
 */
public class HttpRequest implements Request {
	private final HttpURLConnection httpURLConnection;

	public HttpRequest(HttpURLConnection httpURLConnection) {
		super();
		this.httpURLConnection = httpURLConnection;
	}

	public String[] getHeaderKeys() {
		return httpURLConnection.getRequestProperties().keySet().toArray(new String[0]);
	}

	public String getRequestHeader(String name) {
		return httpURLConnection.getRequestProperty(name);
	}

	public void setRequestHeader(String name, String value) {
		httpURLConnection.setRequestProperty(name, value);
	}

	public String[] getRequestHeaders(String name) {
		List<String> values = httpURLConnection.getRequestProperties().get(name);
		return values != null ? values.toArray(new String[values.size()]) : null;
	}

	public void setRequestHeaders(String name, String[] values) {
		for (int i = 0; i < values.length; i++) {
			if (i == 0) {
				httpURLConnection.setRequestProperty(name, values[i]);
			} else {
				httpURLConnection.addRequestProperty(name, values[i]);
			}
		}
	}

	public InputStream getRequestInputStream() throws IOException {
		throw new UnsupportedOperationException();
	}

	public OutputStream getRequestOutputStream() throws IOException {
		return httpURLConnection.getOutputStream();
	}

}