package org.qfox.jestful.logging;

import org.qfox.jestful.core.Request;

import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 日志记录输出流
 *
 * @author Payne 646742615@qq.com
 * 2019/8/22 15:33
 */
public class LoggingOutputStream extends FilterOutputStream {
    private final Request request;
    private final ByteArrayOutputStream buf = new ByteArrayOutputStream();
    private boolean closed;

    LoggingOutputStream(OutputStream out, Request request) {
        super(out);
        this.request = request;
    }

    @Override
    public void write(int b) throws IOException {
        buf.write(b);
        out.write(b);
    }

    @Override
    public void write(byte[] b) throws IOException {
        buf.write(b);
        out.write(b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        buf.write(b, off, len);
        out.write(b, off, len);
    }

    @Override
    public void close() throws IOException {
        if (closed) {
            return;
        } else {
            closed = true;
        }
        try {
            String method = request.getMethod();
            String url = request.getURL();
            System.out.println(method + " " + url);

            String[] headerKeys = request.getHeaderKeys();
            for (String headerKey : headerKeys) {
                if (headerKey == null) {
                    continue;
                }
                String[] headerValues = request.getRequestHeaders(headerKey);
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
