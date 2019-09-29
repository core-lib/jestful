package org.qfox.jestful.logging;

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
    private final ByteArrayOutputStream buf = new ByteArrayOutputStream();

    LoggingInputStream(InputStream in) {
        super(in);
    }

    @Override
    public int read() throws IOException {
        int b = super.read();
        if (b != -1) {
            buf.write(b);
        }
        return b;
    }

    @Override
    public int read(byte[] b) throws IOException {
        int len = super.read(b);
        if (len != -1) {
            buf.write(b, 0, len);
        }
        return len;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int _len = super.read(b, off, len);
        if (_len != -1) {
            buf.write(b, 0, _len);
        }
        return _len;
    }

    @Override
    public String toString() {
        return buf.toString();
    }
}
