package org.qfox.jestful.client.cache.impl;

import org.qfox.jestful.client.cache.Data;
import org.qfox.jestful.client.cache.DataStorage;
import org.qfox.jestful.commons.StringKit;

import java.io.*;
import java.net.URLEncoder;

public class FileDataStorage implements DataStorage {
    private final File directory;
    private final String suffix;

    public FileDataStorage() {
        this(".cache");
    }

    public FileDataStorage(String suffix) {
        this(System.getProperty("java.io.tmpdir") + File.separator + "jestful-cache" + File.separator, suffix);
    }

    public FileDataStorage(String path, String suffix) {
        this(new File(path), suffix);
    }

    public FileDataStorage(File directory, String suffix) {
        if (directory == null) throw new NullPointerException("directory == null");
        if (directory.isFile()) throw new IllegalArgumentException("directory is a file");
        if (!directory.exists() && !directory.mkdirs()) throw new IllegalStateException("could not create directory: " + directory);
        this.directory = directory;
        this.suffix = suffix != null ? suffix.startsWith(".") ? suffix : '.' + suffix : "";
    }

    private static String normalize(String key) throws UnsupportedEncodingException {
        if (key == null) throw new NullPointerException();
        StringBuilder path = new StringBuilder();
        String[] parts = key.split("/+");
        for (String part : parts) {
            part = URLEncoder.encode(part, "UTF-8");
            if (part.length() > 255) part = StringKit.md5Hex(part);
            path.append(File.separator).append(part);
        }
        return path.toString();
    }

    @Override
    public Data get(String key) throws IOException {
        String path = normalize(key);
        File file = new File(directory, path + suffix);
        if (file.exists() && file.isFile()) return new FileData(file);
        else return null;
    }

    @Override
    public Data alloc(String key) throws IOException {
        String path = normalize(key);
        File file = new File(directory, path + suffix);
        File directory = file.getParentFile();
        if (!directory.exists() && !directory.mkdirs()) throw new IOException("could not create directory: " + directory);
        return new FileData(file);
    }

    private static class FileData implements Data {
        private final File file;

        FileData(File file) {
            if (file == null) throw new NullPointerException();
            this.file = file;
        }

        @Override
        public void read(Reader reader) throws IOException {
            reader.read(new RandomAccessFileInputStream(file));
        }

        @Override
        public void write(Writer writer) throws IOException {
            writer.write(new RandomAccessFileOutputStream(file));
        }

        @Override
        public void free() throws IOException {

        }
    }

    private static class RandomAccessFileInputStream extends InputStream {
        private final RandomAccessFile raf;

        RandomAccessFileInputStream(File file) throws IOException {
            this.raf = new RandomAccessFile(file, "rws");
        }

        @Override
        public int read() throws IOException {
            return raf.read();
        }

        @Override
        public int read(byte[] b) throws IOException {
            return raf.read(b);
        }

        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            return raf.read(b, off, len);
        }

        @Override
        public long skip(long n) throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public int available() throws IOException {
            return -1;
        }

        @Override
        public void close() throws IOException {
            raf.close();
        }

        @Override
        public void mark(int readlimit) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void reset() throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean markSupported() {
            return false;
        }

    }

    private static class RandomAccessFileOutputStream extends OutputStream {
        private final RandomAccessFile raf;

        RandomAccessFileOutputStream(File file) throws IOException {
            this.raf = new RandomAccessFile(file, "rws");
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
}
