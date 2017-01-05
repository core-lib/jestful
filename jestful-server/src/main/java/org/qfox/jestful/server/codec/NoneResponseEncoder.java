package org.qfox.jestful.server.codec;

import org.qfox.jestful.core.Encoding;
import org.qfox.jestful.core.codec.ResponseEncoder;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by yangchangpei on 17/1/5.
 */
public class NoneResponseEncoder implements ResponseEncoder {
    private final String contentEncoding = "none";

    @Override
    public String getContentEncoding() {
        return contentEncoding;
    }

    @Override
    public OutputStream wrap(OutputStream source, Encoding encoding) throws IOException {
        return source;
    }
}
