package org.qfox.jestful.logging;

import org.qfox.jestful.core.Response;
import org.qfox.jestful.core.Status;

import java.io.ByteArrayOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 日志记录输入流
 *
 * @author Payne 646742615@qq.com
 * 2019/8/22 15:36
 */
public class LoggingInputStream extends FilterInputStream {
    private final Response response;
    private final ByteArrayOutputStream buf = new ByteArrayOutputStream();
    private boolean closed;

    LoggingInputStream(InputStream in, Response response) {
        super(in);
        this.response = response;
    }

    @Override
    public int read() throws IOException {
        int b = super.read();
        buf.write(b);
        return b;
    }

    @Override
    public int read(byte[] b) throws IOException {
        int len = super.read(b);
        buf.write(b, 0, len);
        return len;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int l = super.read(b, off, len);
        buf.write(b, 0, l);
        return len;
    }

    @Override
    public void close() throws IOException {
        if (closed) {
            return;
        } else {
            closed = true;
        }
        try {
            Status status = response.getResponseStatus();
            String[] headerKeys = response.getHeaderKeys();
            System.out.println(status);
            for (String headerKey : headerKeys) {
                if (headerKey == null) {
                    continue;
                }
                String[] headerValues = response.getResponseHeaders(headerKey);
                for (String headerValue : headerValues) {
                    System.out.println(headerKey + ": " + headerValue);
                }
            }
            System.out.println();
            System.out.write(buf.toByteArray());
            System.out.println();
            System.out.println();
        } finally {
            super.close();
        }
    }
}
