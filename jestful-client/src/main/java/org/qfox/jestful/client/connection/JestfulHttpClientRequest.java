package org.qfox.jestful.client.connection;

import org.qfox.jestful.core.Request;

import java.io.*;
import java.net.HttpURLConnection;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

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
 * @date 2016年4月27日 下午9:17:47
 * @since 1.0.0
 */
public class JestfulHttpClientRequest implements Request {
    private final HttpURLConnection httpURLConnection;
    private String characterEncoding;
    private OutputStream outputStream;
    private Writer writer;
    private boolean connected;
    private Map<String, List<String>> headers;

    public JestfulHttpClientRequest(HttpURLConnection httpURLConnection) {
        super();
        this.httpURLConnection = httpURLConnection;
    }

    @Override
    public String getURL() {
        return httpURLConnection.getURL().toString();
    }

    @Override
    public String getMethod() {
        return httpURLConnection.getRequestMethod();
    }

    public String[] getHeaderKeys() {
        if (connected) {
            return headers.keySet().toArray(new String[0]);
        }
        return httpURLConnection.getRequestProperties().keySet().toArray(new String[0]);
    }

    public String getRequestHeader(String name) {
        if (connected) {
            List<String> values = headers.get(name);
            return values == null || values.isEmpty() ? null : values.iterator().next();
        }
        return httpURLConnection.getRequestProperty(name);
    }

    public void setRequestHeader(String name, String value) {
        httpURLConnection.setRequestProperty(name, value);
    }

    public String[] getRequestHeaders(String name) {
        if (connected) {
            List<String> values = headers.get(name);
            return values != null ? values.toArray(new String[0]) : null;
        }
        List<String> values = httpURLConnection.getRequestProperties().get(name);
        return values != null ? values.toArray(new String[0]) : null;
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

    public int getConnTimeout() {
        return httpURLConnection.getConnectTimeout();
    }

    public void setConnTimeout(int timeout) {
        httpURLConnection.setConnectTimeout(timeout);
    }

    public int getReadTimeout() {
        return httpURLConnection.getReadTimeout();
    }

    public void setReadTimeout(int timeout) {
        httpURLConnection.setReadTimeout(timeout);
    }

    public int getWriteTimeout() {
        return 0;
    }

    public void setWriteTimeout(int timeout) {

    }

    public String getContentType() {
        return getRequestHeader("Content-Type");
    }

    public void setContentType(String type) {
        setRequestHeader("Content-Type", type);
    }

    public String getCharacterEncoding() {
        return characterEncoding;
    }

    public void setCharacterEncoding(String env) throws UnsupportedEncodingException {
        if (Charset.isSupported(env)) {
            characterEncoding = env;
        } else {
            throw new UnsupportedEncodingException(env);
        }
    }

    public void connect() throws IOException {
        if (!connected) {
            connected = true;
            headers = httpURLConnection.getRequestProperties();
        }
        httpURLConnection.connect();
    }

    public void close() throws IOException {
        httpURLConnection.disconnect();
    }

    public InputStream getRequestInputStream() {
        throw new UnsupportedOperationException();
    }

    public OutputStream getRequestOutputStream() throws IOException {
        if (outputStream != null) {
            return outputStream;
        }
        if (!connected) {
            connected = true;
            headers = httpURLConnection.getRequestProperties();
        }
        return outputStream = httpURLConnection.getOutputStream();
    }

    @Override
    public Reader getRequestReader() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Writer getRequestWriter() throws IOException {
        if (writer != null) {
            return writer;
        }
        OutputStream out = getRequestOutputStream();
        return writer = characterEncoding != null ? new OutputStreamWriter(out, characterEncoding) : new OutputStreamWriter(out);
    }
}
