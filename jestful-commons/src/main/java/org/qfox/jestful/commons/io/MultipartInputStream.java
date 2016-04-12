package org.qfox.jestful.commons.io;

import java.io.IOException;
import java.io.InputStream;

import org.qfox.jestful.commons.Multipart;

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
public class MultipartInputStream extends InputStream {
	private final InputStream inputStream;
	private final String boundary;
	private final byte[] buffer;
	private int length = 0;
	private boolean end;
	private boolean start;

	public MultipartInputStream(InputStream inputStream, String boundary) throws IOException {
		super();
		if (inputStream == null || boundary == null) {
			throw new NullPointerException();
		}
		this.inputStream = inputStream;
		this.boundary = "--" + boundary;
		this.buffer = new byte[boundary.length() + 5];
		String line = IOUtils.readLine(inputStream);
		// 如果后面跟换行那么证明还有更多数据
		if (line.equals(this.boundary)) {
			start = false;
		}
		// 如果跟的是 "--" 证明已经读完了
		else if (line.equals(this.boundary + "--")) {
			end = true;
			start = false;
		} else {
			throw new IOException("wrong format of stream content");
		}
	}

	public Multipart getNextMultipart() throws IOException {
		if (end) {
			return null;
		}
		while (end == false && start == true) {
			read();
		}
		if (end) {
			return null;
		}
		Multipart multipart = new Multipart(inputStream);
		start = true;
		return multipart;
	}

	@Override
	public int read() throws IOException {
		if (end == true || start == false) {
			return -1;
		}
		int b = 0;
		if (length > 0) {
			b = buffer[0] & 0xff;
			length--;
			// 前移
			for (int i = 0; i < length; i++) {
				buffer[i] = buffer[i + 1];
			}
		} else {
			b = inputStream.read();
		}

		if (b == '\r') {
			int len = length + inputStream.read(buffer, length, buffer.length - length);
			String line = "\r" + new String(buffer, 0, len);
			// 如果后面跟换行那么证明还有更多数据
			if (len == buffer.length && line.equals("\r\n" + boundary + "\r\n")) {
				start = false;
				length = 0;
				return -1;
			}
			// 如果跟的是 "--" 证明已经读完了
			else if (len == buffer.length && line.equals("\r\n" + boundary + "--")) {
				end = true;
				length = 0;
				return -1;
			}
			// 都不是那就是数据本身带有'\r'
			else {
				length = len;
				return b;
			}
		}
		return b;
	}

	@Override
	public void close() throws IOException {
		close(false);
	}

	public void close(boolean force) throws IOException {
		if (end || force) {
			super.close();
			inputStream.close();
		}
	}

}
