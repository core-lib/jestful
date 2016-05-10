package org.qfox.jestful.commons.io;

import java.io.IOException;
import java.io.OutputStream;

/**
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 * 
 * @author Payne 646742615@qq.com
 *
 * @date 2016年5月10日 下午5:43:45
 *
 * @since 1.0.0
 */
public abstract class LazyOutputStream extends OutputStream {
	private OutputStream outputStream;
	private final Object lock = new Object();

	@Override
	public void write(int b) throws IOException {
		get().write(b);
	}

	@Override
	public void write(byte[] b) throws IOException {
		get().write(b);
	}

	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		get().write(b, off, len);
	}

	@Override
	public void flush() throws IOException {
		get().flush();
	}

	@Override
	public void close() throws IOException {
		get().close();
	}

	private final OutputStream get() {
		if (outputStream != null) {
			return outputStream;
		}
		synchronized (lock) {
			if (outputStream != null) {
				return outputStream;
			}
			try {
				return outputStream = getOutputStream();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	protected abstract OutputStream getOutputStream() throws IOException;

}
