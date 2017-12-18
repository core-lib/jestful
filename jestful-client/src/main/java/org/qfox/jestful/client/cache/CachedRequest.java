package org.qfox.jestful.client.cache;

import org.qfox.jestful.core.Request;
import org.qfox.jestful.core.RequestWrapper;

import java.io.*;

class CachedRequest extends RequestWrapper {
    private final ByteArrayOutputStream body = new ByteArrayOutputStream();

    CachedRequest(Request request) {
        super(request);
    }

    @Override
    public void connect() throws IOException {
    }

    @Override
    public OutputStream getRequestOutputStream() throws IOException {
        return body;
    }

    InputStream getRequestBodyInputStream() {
        return new ByteArrayInputStream(body.toByteArray());
    }

}
