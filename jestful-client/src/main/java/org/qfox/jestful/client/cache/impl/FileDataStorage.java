package org.qfox.jestful.client.cache.impl;

import org.qfox.jestful.client.cache.Data;
import org.qfox.jestful.client.cache.DataStorage;
import org.qfox.jestful.commons.StringKit;
import org.qfox.jestful.commons.io.RandomAccessFileInputStream;
import org.qfox.jestful.commons.io.RandomAccessFileOutputStream;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
            reader.read(new RandomAccessFileInputStream(file, "rws"));
        }

        @Override
        public void write(Writer writer) throws IOException {
            writer.write(new RandomAccessFileOutputStream(file, "rws"));
        }

        @Override
        public void free() throws IOException {

        }
    }

}
