package org.qfox.jestful.server.codec;

import org.qfox.jestful.core.Encoding;
import org.qfox.jestful.core.codec.ResponseEncoder;

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

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
