package org.qfox.jestful.commons.io;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
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
	 * close an {@link Closeable} I/O stream quietly or not quietly by parameter
	 * quietly
	 * 
	 * @param closeable
	 *            I/O stream
	 * @param quietly
	 *            if true : catch and ignore all exceptions <br/>
	 *            if false : throw a runtime exception to wrap the exception
	 *            caught
	 * @throws RuntimeException
	 *             exception thrown only quietly is true and {@link IOException}
	 *             thrown when closing the I/O stream
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

	public static String readLine(InputStream in) throws IOException {
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

}
