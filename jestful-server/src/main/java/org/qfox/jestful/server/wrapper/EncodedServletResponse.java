package org.qfox.jestful.server.wrapper;

import java.io.IOException;
import java.io.OutputStream;

import org.qfox.jestful.core.Encoding;
import org.qfox.jestful.core.codec.ResponseEncoder;
import org.qfox.jestful.server.JestfulServletResponse;
import org.qfox.jestful.server.JestfulServletResponseWrapper;

public class EncodedServletResponse extends JestfulServletResponseWrapper {
	private final Encoding encoding;
	private final ResponseEncoder encoder;

	public EncodedServletResponse(JestfulServletResponse response, Encoding encoding, ResponseEncoder encoder) {
		super(response);
		this.encoding = encoding;
		this.encoder = encoder;
	}

	@Override
	public OutputStream getResponseOutputStream() throws IOException {
		OutputStream out = super.getResponseOutputStream();
		return encoder.wrap(out, encoding);
	}

}
