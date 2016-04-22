package org.qfox.jestful.commons.io;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * <p>
 * Description: A convenient tool for I/O operations
 * </p>
 * 
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 * 
 * @author Payne 646742615@qq.com
 *
 * @date 2016年3月17日 下午3:48:07
 *
 * @since 1.0.0
 */
public class IOUtils {

	/**
	 * just close the I/O stream quietly
	 * 
	 * @see {@link IOUtils#close(Closeable, boolean)}
	 * @param closeable
	 */
	public static void close(Closeable closeable) {
		close(closeable, true);
	}

	/**
	 * close an {@link Closeable} I/O stream quietly or not quietly by parameter quietly
	 * 
	 * @param closeable
	 *            I/O stream
	 * @param quietly
	 *            if true : catch and ignore all exceptions <br/>
	 *            if false : throw a runtime exception to wrap the exception caught
	 * @throws RuntimeException
	 *             exception thrown only quietly is true and {@link IOException} thrown when closing the I/O stream
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

	public static void transfer(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[4096];
		int length = 0;
		while ((length = in.read(buffer)) != -1) {
			out.write(buffer, 0, length);
		}
	}

	public static void transfer(InputStream in, File target) throws IOException {
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(target);
			transfer(in, out);
		} finally {
			out.close();
		}
	}

	public static void transfer(File source, OutputStream out) throws IOException {
		FileInputStream in = null;
		try {
			in = new FileInputStream(source);
			transfer(in, out);
		} finally {
			in.close();
		}
	}

	public static void transfer(File source, File target) throws IOException {
		FileInputStream in = null;
		FileOutputStream out = null;
		try {
			in = new FileInputStream(source);
			out = new FileOutputStream(target);
			transfer(in, out);
		} finally {
			in.close();
			out.close();
		}
	}

}
