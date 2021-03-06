package org.qfox.jestful.client.codec;

import org.qfox.jestful.core.Encoding;
import org.qfox.jestful.core.codec.RequestEncoder;

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.DeflaterOutputStream;

public class DeflateRequestEncoder implements RequestEncoder {
    private final String contentEncoding = "deflate";

    public String getContentEncoding() {
        return contentEncoding;
    }

    public OutputStream wrap(OutputStream source, Encoding encoding) throws IOException {
        return new DeflaterOutputStream(source);
    }

}
