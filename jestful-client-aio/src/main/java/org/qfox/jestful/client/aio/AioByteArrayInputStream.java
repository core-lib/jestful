package org.qfox.jestful.client.aio;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Created by yangchangpei on 17/3/24.
 */
public class AioByteArrayInputStream extends ByteArrayInputStream {
    private boolean closed = false;

    public AioByteArrayInputStream(byte[] buf) {
        this(buf, 0, buf.length);
    }

    public AioByteArrayInputStream(byte[] buf, int offset, int length) {
        super(buf, offset, length);
    }

    @Override
    public int read() {
        check();
        return super.read();
    }

    @Override
    public int read(byte[] b) throws IOException {
        check();
        return super.read(b);
    }

    @Override
    public int read(byte[] b, int off, int len) {
        check();
        return super.read(b, off, len);
    }

    @Override
    public long skip(long n) {
        check();
        return super.skip(n);
    }

    @Override
    public int available() {
        check();
        return super.available();
    }

    @Override
    public boolean markSupported() {
        check();
        return super.markSupported();
    }

    @Override
    public void mark(int readAheadLimit) {
        check();
        super.mark(readAheadLimit);
    }

    @Override
    public void reset() {
        closed = false;
        super.reset();
    }

    @Override
    public void close() throws IOException {
        closed = true;
    }

    protected void check() throws IllegalStateException {
        if (closed) throw new IllegalStateException("Stream Closed");
    }

}
