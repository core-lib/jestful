package org.qfox.jestful.client.codec;

import org.qfox.jestful.core.Encoding;
import org.qfox.jestful.core.codec.RequestEncoder;

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

public class GzipRequestEncoder implements RequestEncoder {
    private final String contentEncoding = "gzip";

    public String getContentEncoding() {
        return contentEncoding;
    }

    public OutputStream wrap(OutputStream source, Encoding encoding) throws IOException {
        return new GZIPOutputStream(source);
    }

}
