package org.qfox.jestful.server.codec;

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

import org.qfox.jestful.core.Encoding;
import org.qfox.jestful.core.codec.ResponseEncoder;

public class GzipResponseEncoder implements ResponseEncoder {
	private final String contentEncoding = "gzip";

	public String getContentEncoding() {
		return contentEncoding;
	}

	public OutputStream wrap(OutputStream source, Encoding encoding)
			throws IOException {
		return new GZIPOutputStream(source);
	}

}
