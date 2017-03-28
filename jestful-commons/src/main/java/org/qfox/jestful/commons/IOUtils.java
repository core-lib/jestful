package org.qfox.jestful.commons;

import java.io.*;

/**
 * <p>
 * Description: A convenient tool for I/O operations
 * </p>
 * <p>
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 *
 * @author Payne 646742615@qq.com
 * @date 2016年3月17日 下午3:48:07
 * @since 1.0.0
 */
public class IOUtils {

    /**
     * just close the I/O stream quietly
     *
     * @param closeable
     * @see {@link IOUtils#close(Closeable, boolean)}
     */
    public static void close(Closeable closeable) {
        close(closeable, true);
    }

    /**
     * close an {@link Closeable} I/O stream quietly or not quietly by parameter
     * quietly
     *
     * @param closeable I/O stream
     * @param quietly   if true : catch and ignore all exceptions <br/>
     *                  if false : throw a runtime exception to wrap the exception
     *                  caught
     * @throws RuntimeException exception thrown only quietly is true and {@link IOException}
     *                          thrown when closing the I/O stream
     */
    public static void close(Closeable closeable, boolean quietly) throws RuntimeException {
        if (closeable == null) {
            return;
        }
        try {
            closeable.close();
        } catch (IOException e) {
            if (quietly) {
                return;
            }
            throw new RuntimeException(e);
        }
    }

    public static String readln(InputStream in) throws IOException {
        int b = in.read();
        if (b == -1) {
            return null;
        }
        StringWriter writer = new StringWriter();
        while (b != -1) {
            switch (b) {
                case '\r':
                    break;
                case '\n':
                    return writer.toString();
                default:
                    writer.write(b);
                    break;
            }
            b = in.read();
        }
        return writer.toString();
    }

    public static String readln(Reader reader) throws Exception {
        int b = reader.read();
        if (b == -1) {
            return null;
        }
        StringWriter writer = new StringWriter();
        while (b != -1) {
            switch (b) {
                case '\r':
                    break;
                case '\n':
                    return writer.toString();
                default:
                    writer.write(b);
                    break;
            }
            b = reader.read();
        }
        return writer.toString();
    }

    public static void writeln(String line, OutputStream out) throws IOException {
        if (line == null) {
            return;
        }
        StringReader reader = new StringReader(line);
        int b = 0;
        while ((b = reader.read()) != -1) {
            out.write(b);
        }
        out.write('\r');
        out.write('\n');
    }

    public static void writeln(String line, Writer writer) throws IOException {
        if (line == null) {
            return;
        }
        StringReader reader = new StringReader(line);
        int b = 0;
        while ((b = reader.read()) != -1) {
            writer.write(b);
        }
        writer.write('\r');
        writer.write('\n');
    }

    public static long transfer(InputStream in, int length, OutputStream out) throws IOException {
        int total = 0;
        byte[] buffer = new byte[4096];
        int len = 0;
        while ((len = in.read(buffer, 0, Math.min(length - total, buffer.length))) != -1) {
            out.write(buffer, 0, len);
            total += len;
            if (total == length) {
                break;
            }
        }
        return total;
    }

    public static long transfer(InputStream in, OutputStream out) throws IOException {
        long total = 0;
        byte[] buffer = new byte[4096];
        int length = 0;
        while ((length = in.read(buffer)) != -1) {
            out.write(buffer, 0, length);
            total += length;
        }
        return total;
    }

    public static long transfer(Reader reader, Writer writer) throws IOException {
        long total = 0;
        char[] buffer = new char[4096];
        int length = 0;
        while ((length = reader.read(buffer)) != -1) {
            writer.write(buffer, 0, length);
            total += length;
        }
        return total;
    }

    public static long transfer(InputStream in, File target) throws IOException {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(target);
            return transfer(in, out);
        } finally {
            close(out);
        }
    }

    public static long transfer(Reader reader, File target) throws IOException {
        FileOutputStream out = null;
        OutputStreamWriter writer = null;
        try {
            out = new FileOutputStream(target);
            writer = new OutputStreamWriter(out);
            return transfer(reader, writer);
        } finally {
            close(writer);
            close(out);
        }
    }

    public static long transfer(File source, OutputStream out) throws IOException {
        FileInputStream in = null;
        try {
            in = new FileInputStream(source);
            return transfer(in, out);
        } finally {
            close(in);
        }
    }

    public static long transfer(File source, Writer writer) throws IOException {
        FileInputStream in = null;
        InputStreamReader reader = null;
        try {
            in = new FileInputStream(source);
            reader = new InputStreamReader(in);
            return transfer(reader, writer);
        } finally {
            close(reader);
            close(in);
        }
    }

    public static long transfer(File source, File target) throws IOException {
        FileInputStream in = null;
        FileOutputStream out = null;
        try {
            in = new FileInputStream(source);
            out = new FileOutputStream(target);
            return transfer(in, out);
        } finally {
            close(in);
            close(out);
        }
    }

    public static String toString(InputStream in) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        transfer(in, baos);
        return baos.toString();
    }

    public static String toString(Reader reader) throws IOException {
        StringWriter writer = new StringWriter();
        transfer(reader, writer);
        return writer.toString();
    }

}
