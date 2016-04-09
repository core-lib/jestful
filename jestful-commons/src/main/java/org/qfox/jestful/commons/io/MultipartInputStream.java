package org.qfox.jestful.commons.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.NoSuchElementException;

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
 * @date 2016年4月9日 下午6:54:44
 *
 * @since 1.0.0
 */
public class MultipartInputStream extends InputStream implements Iterator<Multipart> {
	private final InputStream inputStream;
	private final char[] boundary;
	private boolean end;

	public MultipartInputStream(InputStream inputStream, char[] boundary) {
		super();
		if (inputStream == null || boundary == null) {
			throw new NullPointerException();
		}
		this.inputStream = inputStream;
		this.boundary = boundary;
	}

	public MultipartInputStream(InputStream inputStream, String boundary) {
		this(inputStream, boundary.toCharArray());
	}

	public boolean hasNext() {
		return false;
	}

	public Multipart next() {
		if (hasNext()) {

			return null;
		} else {
			throw new NoSuchElementException();
		}
	}

	@Override
	public int read() throws IOException {
		return end ? -1 : inputStream.read();
	}

}
