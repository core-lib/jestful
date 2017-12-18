package org.qfox.jestful.commons.io;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class BackupInputStream extends FilterInputStream implements IOConstants {
    private final OutputStream backup;

    public BackupInputStream(InputStream in, OutputStream backup) {
        super(in);
        this.backup = backup;
    }

    @Override
    public int read() throws IOException {
        int b = super.read();
        if (b != EOF) backup.write(b & 0xFF);
        return b;
    }

    @Override
    public int read(byte[] b) throws IOException {
        int length = super.read(b);
        if (length != EOF) backup.write(b, 0, length);
        return length;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int length = super.read(b, off, len);
        if (length != EOF) backup.write(b, off, length);
        return length;
    }

}