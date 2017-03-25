package org.qfox.jestful.client.nio;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * Created by yangchangpei on 17/3/24.
 */
public class NioByteArrayOutputStream extends ByteArrayOutputStream {
    private boolean closed = false;

    public NioByteArrayOutputStream() {
        this(32);
    }

    public NioByteArrayOutputStream(int size) {
        super(size);
    }

    @Override
    public void write(int b) {
        check();
        super.write(b);
    }

    @Override
    public void write(byte[] b) throws IOException {
        check();
        super.write(b);
    }


    @Override
    public void write(byte[] b, int off, int len) {
        check();
        super.write(b, off, len);
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

    public ByteBuffer toByteBuffer() {
        return ByteBuffer.wrap(buf, 0, count);
    }

    public InputStream toInputStream() {
        return new ByteArrayInputStream(buf, 0, count);
    }

}
