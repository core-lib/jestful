package org.qfox.jestful.server.wrapper;

import java.io.IOException;
import java.io.InputStream;

import org.qfox.jestful.core.Encoding;
import org.qfox.jestful.core.codec.RequestDecoder;
import org.qfox.jestful.server.JestfulServletRequest;
import org.qfox.jestful.server.JestfulServletRequestWrapper;

public class DecodedServletRequest extends JestfulServletRequestWrapper {
	private final Encoding encoding;
	private final RequestDecoder decoder;

	public DecodedServletRequest(JestfulServletRequest request, Encoding encoding, RequestDecoder decoder) {
		super(request);
		this.encoding = encoding;
		this.decoder = decoder;
	}

	@Override
	public InputStream getRequestInputStream() throws IOException {
		InputStream in = super.getRequestInputStream();
		return decoder.wrap(in, encoding);
	}

}
