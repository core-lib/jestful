package org.qfox.jestful.server.codec;

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.DeflaterOutputStream;

import org.qfox.jestful.core.Encoding;
import org.qfox.jestful.core.codec.ResponseEncoder;

public class DeflateResponseEncoder implements ResponseEncoder {
	private final String contentEncoding = "deflate";

	public String getContentEncoding() {
		return contentEncoding;
	}

	public OutputStream wrap(OutputStream source, Encoding encoding)
			throws IOException {
		return new DeflaterOutputStream(source);
	}

}
