package org.qfox.jestful.client.wrapper;

import org.qfox.jestful.core.Encoding;
import org.qfox.jestful.core.Request;
import org.qfox.jestful.core.RequestWrapper;
import org.qfox.jestful.core.codec.RequestEncoder;

import java.io.IOException;
import java.io.OutputStream;

public class EncodedRequest extends RequestWrapper {
    private final Encoding encoding;
    private final RequestEncoder encoder;

    public EncodedRequest(Request request, Encoding encoding, RequestEncoder encoder) {
        super(request);
        this.encoding = encoding;
        this.encoder = encoder;
    }

    @Override
    public OutputStream getRequestOutputStream() throws IOException {
        this.setRequestHeader("Content-Encoding", encoding.toString(false));
        OutputStream out = super.getRequestOutputStream();
        return encoder.wrap(out, encoding);
    }

}
