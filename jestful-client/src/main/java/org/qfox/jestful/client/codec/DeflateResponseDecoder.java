package org.qfox.jestful.client.codec;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.InflaterInputStream;

import org.qfox.jestful.core.Encoding;
import org.qfox.jestful.core.codec.ResponseDecoder;

public class DeflateResponseDecoder implements ResponseDecoder {
	private final String contentEncoding = "deflate";

	public String getContentEncoding() {
		return contentEncoding;
	}

	public InputStream wrap(InputStream source, Encoding encoding) throws IOException {
		return new InflaterInputStream(source);
	}

}
