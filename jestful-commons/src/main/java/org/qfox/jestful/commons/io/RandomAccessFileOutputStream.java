package org.qfox.jestful.commons.io;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;

public class RandomAccessFileOutputStream extends OutputStream {
    private final RandomAccessFile raf;

    public RandomAccessFileOutputStream(File file, String mode) throws IOException {
        this(new RandomAccessFile(file, mode));
    }

    public RandomAccessFileOutputStream(RandomAccessFile raf) {
        this.raf = raf;
    }

    @Override
    public void write(int b) throws IOException {
        raf.write(b);
    }

    @Override
    public void write(byte[] b) throws IOException {
        raf.write(b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        raf.write(b, off, len);
    }

    @Override
    public void flush() throws IOException {
    }

    @Override
    public void close() throws IOException {
        raf.close();
    }
}