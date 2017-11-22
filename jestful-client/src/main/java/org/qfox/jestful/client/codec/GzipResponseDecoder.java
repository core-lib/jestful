package org.qfox.jestful.client.codec;

import org.qfox.jestful.core.Encoding;
import org.qfox.jestful.core.codec.ResponseDecoder;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

public class GzipResponseDecoder implements ResponseDecoder {
    private final String contentEncoding = "gzip";

    public String getContentEncoding() {
        return contentEncoding;
    }

    public InputStream wrap(InputStream source, Encoding encoding) throws IOException {
        return new GZIPInputStream(source);
    }

}
