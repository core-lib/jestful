package org.qfox.jestful.commons.io;

import java.io.IOException;
import java.io.InputStream;

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
 * @date 2016年5月10日 下午5:34:37
 *
 * @since 1.0.0
 */
public abstract class LazyInputStream extends InputStream {
	private InputStream inputSream;
	private final Object lock = new Object();

	@Override
	public int read() throws IOException {
		return get().read();
	}

	@Override
	public int read(byte[] b) throws IOException {
		return get().read(b);
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		return get().read(b, off, len);
	}

	@Override
	public long skip(long n) throws IOException {
		return get().skip(n);
	}

	@Override
	public int available() throws IOException {
		return get().available();
	}

	@Override
	public void close() throws IOException {
		get().close();
	}

	@Override
	public synchronized void mark(int readlimit) {
		get().mark(readlimit);
	}

	@Override
	public synchronized void reset() throws IOException {
		get().reset();
	}

	@Override
	public boolean markSupported() {
		return get().markSupported();
	}

	private final InputStream get() {
		if (inputSream != null) {
			return inputSream;
		}
		synchronized (lock) {
			if (inputSream != null) {
				return inputSream;
			}
			try {
				return inputSream = getInputStream();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	protected abstract InputStream getInputStream() throws IOException;

}
