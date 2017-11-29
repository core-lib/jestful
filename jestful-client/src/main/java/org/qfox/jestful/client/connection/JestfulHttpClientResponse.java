package org.qfox.jestful.client.connection;

import org.qfox.jestful.core.Response;
import org.qfox.jestful.core.Status;
import org.qfox.jestful.core.http.HttpStatus;

import java.io.*;
import java.net.HttpURLConnection;
import java.nio.charset.Charset;
import java.util.List;

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
 * @date 2016年4月27日 下午9:30:33
 * @since 1.0.0
 */
public class JestfulHttpClientResponse implements Response {
    private final HttpURLConnection httpURLConnection;
    private String characterEncoding;
    private InputStream inputStream;
    private Reader reader;

    public JestfulHttpClientResponse(HttpURLConnection httpURLConnection) {
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
        if (inputStream != null) {
            return inputStream;
        }
        if (isResponseSuccess()) {
            return inputStream = httpURLConnection.getInputStream();
        } else {
            return inputStream = httpURLConnection.getErrorStream();
        }
    }

    public OutputStream getResponseOutputStream() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Reader getResponseReader() throws IOException {
        if (reader != null) {
            return reader;
        }
        InputStream in = getResponseInputStream();
        return reader = characterEncoding != null ? new InputStreamReader(in, characterEncoding) : new InputStreamReader(in);
    }

    @Override
    public Writer getResponseWriter() throws IOException {
        throw new UnsupportedOperationException();
    }

    public Status getResponseStatus() throws IOException {
        int code = httpURLConnection.getResponseCode();
        String reason = httpURLConnection.getResponseMessage();
        return new HttpStatus(code, reason);
    }

    public void setResponseStatus(Status status) throws IOException {
        throw new UnsupportedOperationException();
    }

    public boolean isResponseSuccess() throws IOException {
        Status status = getResponseStatus();
        return status.isSuccess();
    }

    public String getContentType() {
        return httpURLConnection.getContentType();
    }

    public void setContentType(String type) {
        throw new UnsupportedOperationException();
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

    @Override
    public boolean isCommitted() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void reset() {
        throw new UnsupportedOperationException();
    }

    public void close() throws IOException {
        httpURLConnection.disconnect();
    }

}
