package org.qfox.jestful.commons.io;

import java.io.IOException;
import java.io.OutputStream;

public class NoneOutputStream extends OutputStream {

    @Override
    public void write(int b) throws IOException {

    }

    @Override
    public void write(byte[] b) throws IOException {
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
    }

    @Override
    public void flush() throws IOException {
    }

    @Override
    public void close() throws IOException {
    }

}
