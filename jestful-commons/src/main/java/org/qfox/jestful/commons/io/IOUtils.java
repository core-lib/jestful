package org.qfox.jestful.commons.io;

import java.io.Closeable;
import java.io.IOException;

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
	public void close(Closeable closeable) {
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
	public void close(Closeable closeable, boolean quietly) throws RuntimeException {
		try {
			closeable.close();
		} catch (IOException e) {
			if (quietly) {
				return;
			}
			throw new RuntimeException(e);
		}
	}

}
