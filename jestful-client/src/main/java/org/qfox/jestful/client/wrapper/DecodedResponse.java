package org.qfox.jestful.client.wrapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.qfox.jestful.core.Encoding;
import org.qfox.jestful.core.Encodings;
import org.qfox.jestful.core.Response;
import org.qfox.jestful.core.ResponseWrapper;
import org.qfox.jestful.core.codec.ResponseDecoder;

public class DecodedResponse extends ResponseWrapper {
	private final Map<Encoding, ResponseDecoder> decoders;

	public DecodedResponse(Response response, Map<Encoding, ResponseDecoder> decoders) {
		super(response);
		this.decoders = decoders;
	}

	@Override
	public InputStream getResponseInputStream() throws IOException {
		InputStream in = super.getResponseInputStream();
		String contentEncoding = getResponseHeader("Content-Encoding");
		if (contentEncoding == null || contentEncoding.length() == 0) {
			return in;
		}
		Encoding encoding = Encoding.valueOf(contentEncoding);
		if (decoders.containsKey(encoding)) {
			ResponseDecoder decoder = decoders.get(encoding);
			return decoder.wrap(in, encoding);
		} else {
			throw new IOException("unexpected content encoding " + contentEncoding + " while it accept " + new Encodings(decoders.keySet()));
		}
	}

}
