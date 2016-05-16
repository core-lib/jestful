package org.qfox.jestful.server.codec;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import org.qfox.jestful.core.Encoding;
import org.qfox.jestful.core.codec.RequestDecoder;

public class GzipRequestDecoder implements RequestDecoder {
	private final String contentEncoding = "gzip";

	public String getContentEncoding() {
		return contentEncoding;
	}

	public InputStream wrap(InputStream source, Encoding encoding)
			throws IOException {
		return new GZIPInputStream(source);
	}

}
