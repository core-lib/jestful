package org.qfox.jestful.server.codec;

import org.qfox.jestful.core.Encoding;
import org.qfox.jestful.core.codec.RequestDecoder;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.InflaterInputStream;

public class DeflateRequestDecoder implements RequestDecoder {
    private final String contentEncoding = "deflate";

    public String getContentEncoding() {
        return contentEncoding;
    }

    public InputStream wrap(InputStream source, Encoding encoding)
            throws IOException {
        return new InflaterInputStream(source);
    }

}
