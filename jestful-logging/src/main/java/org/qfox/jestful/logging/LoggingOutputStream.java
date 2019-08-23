package org.qfox.jestful.logging;

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
    private final ByteArrayOutputStream buf = new ByteArrayOutputStream();

    LoggingOutputStream(OutputStream out) {
        super(out);
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
    public String toString() {
        return buf.toString();
    }
}
