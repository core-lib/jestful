package org.qfox.jestful.server.codec;

import org.qfox.jestful.core.Encoding;
import org.qfox.jestful.core.codec.RequestDecoder;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by yangchangpei on 17/1/5.
 */
public class NoneRequestDecoder implements RequestDecoder {
    private final String contentEncoding = "none";

    @Override
    public String getContentEncoding() {
        return contentEncoding;
    }

    @Override
    public InputStream wrap(InputStream source, Encoding encoding) throws IOException {
        return source;
    }
}
