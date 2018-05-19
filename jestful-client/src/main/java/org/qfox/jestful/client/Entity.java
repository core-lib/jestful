package org.qfox.jestful.client;

import org.qfox.jestful.commons.IOKit;
import org.qfox.jestful.core.MediaType;
import org.qfox.jestful.core.Response;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

public final class Entity implements Serializable, Closeable {
    private static final long serialVersionUID = -4968262615254281012L;

    private final MediaType type;
    private final String encoding;
    private final int length;
    private final String charset;
    private final InputStream stream;

    Entity(Response response) throws IOException {
        this.type = response.getContentType() != null ? MediaType.valueOf(response.getContentType()) : null;
        this.encoding = response.getResponseHeader("Transfer-Encoding");
        this.length = response.getResponseHeader("Content-Length") != null ? Integer.valueOf(response.getResponseHeader("Content-Length")) : -1;
        this.charset = type != null && type.getCharset() != null ? type.getCharset() : response.getCharacterEncoding();
        this.stream = response.getResponseInputStream();
    }

    public MediaType getType() {
        return type;
    }

    public String getEncoding() {
        return encoding;
    }

    public int getLength() {
        return length;
    }

    public String getCharset() {
        return charset;
    }

    public InputStream getStream() {
        return stream;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        this.close();
    }

    @Override
    public void close() {
        IOKit.close(stream);
    }

}
