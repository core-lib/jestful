package org.qfox.jestful.client.codec;

import org.qfox.jestful.core.Encoding;
import org.qfox.jestful.core.codec.ResponseDecoder;

import java.io.IOException;
import java.io.InputStream;

public class IdentityResponseDecoder implements ResponseDecoder {
    private final String contentEncoding = "identity";

    public String getContentEncoding() {
        return contentEncoding;
    }

    public InputStream wrap(InputStream source, Encoding encoding) throws IOException {
        return source;
    }

}
