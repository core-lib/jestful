package org.qfox.jestful.client.nio;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by yangchangpei on 17/3/24.
 */
public class CloseableByteArrayOutputStream extends ByteArrayOutputStream {
    private boolean closed = false;

    public CloseableByteArrayOutputStream() {
    }

    public CloseableByteArrayOutputStream(int size) {
        super(size);
    }

    @Override
    public synchronized void write(int b) {
        check();
        super.write(b);
    }

    @Override
    public void write(byte[] b) throws IOException {
        check();
        super.write(b);
    }


    @Override
    public synchronized void write(byte[] b, int off, int len) {
        check();
        super.write(b, off, len);
    }

    @Override
    public synchronized void reset() {
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
