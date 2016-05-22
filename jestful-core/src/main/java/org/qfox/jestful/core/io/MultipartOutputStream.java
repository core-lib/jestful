package org.qfox.jestful.core.io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.qfox.jestful.core.Multihead;

/**
 * <p>
 * Description: multipart/form-data stream writer
 * </p>
 * 
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 * 
 * @author Payne 646742615@qq.com
 *
 * @date 2016年4月15日 上午10:01:04
 *
 * @since 1.0.0
 */
public class MultipartOutputStream extends OutputStream {
	private final OutputStream outputStream;
	private final OutputStreamWriter outputStreamWriter;
	private final String boundary;
	private int parts;
	private boolean closed;

	public MultipartOutputStream(OutputStream outputStream, String boundary) {
		super();
		if (outputStream == null || boundary == null) {
			throw new NullPointerException();
		}
		this.outputStream = outputStream;
		this.outputStreamWriter = new OutputStreamWriter(outputStream);
		this.boundary = boundary;
		this.parts = 0;
		this.closed = false;
	}

	/**
	 * set a multipart/form-data item before call this's write method
	 * 
	 * @param multihead
	 *            multipart/form-data item
	 * @throws IOException
	 *             when IO error occur
	 */
	public void setNextMultihead(Multihead multihead) throws IOException {
		if (closed) {
			throw new IOException("Already closed");
		}
		outputStreamWriter.write(parts++ > 0 ? new char[] { '\r', '\n' } : new char[0]);
		outputStreamWriter.write("--" + boundary + "\r\n");
		multihead.writeTo(outputStreamWriter);
		outputStreamWriter.write(new char[] { '\r', '\n' });
		outputStreamWriter.flush();
	}

	/**
	 * write multipart/form-data item content before call this method please ensure it's already has at least one
	 * mutipart-form item, use {@link MultipartOutputStream#setNextMultihead(Multihead)} to add a mutipart-form item
	 * 
	 * @see {@link MultipartOutputStream#setNextMultihead(Multihead)}
	 */
	@Override
	public void write(int b) throws IOException {
		if (closed) {
			throw new IOException("Already closed");
		}
		if (parts == 0) {
			throw new IOException("No multipart");
		}
		outputStream.write(b);
	}

	/**
	 * write multipart/form-data item content before call this method please ensure it's already has at least one
	 * mutipart-form item, use {@link MultipartOutputStream#setNextMultihead(Multihead)} to add a mutipart-form item
	 * 
	 * @see {@link MultipartOutputStream#setNextMultihead(Multihead)}
	 */
	@Override
	public void write(byte[] b) throws IOException {
		if (closed) {
			throw new IOException("Already closed");
		}
		if (parts == 0) {
			throw new IOException("No multipart");
		}
		outputStream.write(b);
	}

	/**
	 * write multipart/form-data item content before call this method please ensure it's already has at least one
	 * mutipart-form item, use {@link MultipartOutputStream#setNextMultihead(Multihead)} to add a mutipart-form item
	 * 
	 * @see {@link MultipartOutputStream#setNextMultihead(Multihead)}
	 */
	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		if (closed) {
			throw new IOException("Already closed");
		}
		if (parts == 0) {
			throw new IOException("No multipart");
		}
		outputStream.write(b, off, len);
	}

	@Override
	public void flush() throws IOException {
		if (closed) {
			return;
		}
		outputStream.flush();
	}

	/**
	 * {@link MultipartOutputStream#close(boolean)} with {@code false}
	 * 
	 * @see {@link MultipartOutputStream#close(boolean)}
	 */
	@Override
	public void close() throws IOException {
		close(false);
	}

	/**
	 * before close this output stream it will append "--specified boundary--" to the end of target output stream if
	 * forcibly if not it will ignore this call {@link MultipartOutputStream#close()} is equals to
	 * {@link MultipartOutputStream#close(boolean)} with {@code false} parameter
	 * 
	 * @see {@link MultipartOutputStream#close()}
	 */
	public void close(boolean force) throws IOException {
		if (force == false) {
			return;
		}
		if (closed) {
			return;
		}
		try {
			outputStreamWriter.write(parts > 0 ? new char[] { '\r', '\n' } : new char[0]);
			outputStreamWriter.write("--" + boundary + "--");
			outputStreamWriter.write(new char[] { '\r', '\n' });
			outputStreamWriter.flush();
			outputStream.flush();
		} catch (IOException e) {
			throw e;
		} finally {
			closed = true;
			outputStream.close();
			outputStreamWriter.close();
		}
	}

}
