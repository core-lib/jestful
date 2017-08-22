package org.qfox.jestful.server;

import org.qfox.jestful.core.Request;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
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
 * @date 2016年4月8日 下午4:12:32
 * @since 1.0.0
 */
public class JestfulServletRequest extends HttpServletRequestWrapper implements Request {
    private final HttpServletRequest request;
    private HttpSession session;

    public JestfulServletRequest(HttpServletRequest request) {
        super(request);
        this.request = request;
    }

    public String[] getHeaderKeys() {
        Enumeration<String> enumeration = request.getHeaderNames();
        if (enumeration == null) {
            return null;
        }
        List<String> keys = new ArrayList<String>();
        while (enumeration.hasMoreElements()) {
            keys.add(enumeration.nextElement());
        }
        return keys.toArray(new String[keys.size()]);
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

    public int getConnTimeout() {
        return 0;
    }

    public void setConnTimeout(int timeout) {
        throw new UnsupportedOperationException();
    }

    public int getReadTimeout() {
        throw new UnsupportedOperationException();
    }

    public void setReadTimeout(int timeout) {
        throw new UnsupportedOperationException();
    }

    public int getWriteTimeout() {
        throw new UnsupportedOperationException();
    }

    public void setWriteTimeout(int timeout) {
        throw new UnsupportedOperationException();
    }

    public String getContentType() {
        return super.getContentType();
    }

    public void setContentType(String type) {

    }

    public void connect() throws IOException {
        throw new UnsupportedOperationException();
    }

    public void close() throws IOException {

    }

    @Override
    public HttpSession getSession() {
        return getSession(true);
    }

    @Override
    public HttpSession getSession(boolean create) {
        if (session != null) return session;
        HttpSession s = super.getSession(create);
        if (s == null) return null;
        else if (s instanceof JestfulServletSession) return s;
        else return session = new JestfulServletSession(s);
    }

    public InputStream getRequestInputStream() throws IOException {
        return request.getInputStream();
    }

    public OutputStream getRequestOutputStream() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Reader getRequestReader() throws IOException {
        return request.getReader();
    }

    @Override
    public Writer getRequestWriter() throws IOException {
        throw new UnsupportedOperationException();
    }

    public void connect(int timeout) throws IOException {
        throw new UnsupportedOperationException();
    }

}
