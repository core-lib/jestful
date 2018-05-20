package org.qfox.jestful.client;

import org.qfox.jestful.commons.IOKit;
import org.qfox.jestful.core.MediaType;
import org.qfox.jestful.core.Response;

import java.io.*;
import java.nio.charset.Charset;

public final class Entity implements Serializable {
    private static final long serialVersionUID = -4968262615254281012L;

    private final MediaType type;
    private final String charset;
    private final InputStream stream;
    private final int length;

    Entity(Response response) throws IOException {
        this.type = response.getContentType() != null ? MediaType.valueOf(response.getContentType()) : null;
        this.charset = type != null && type.getCharset() != null ? type.getCharset() : response.getCharacterEncoding() != null ? response.getCharacterEncoding() : Charset.defaultCharset().name();
        InputStream in = response.getResponseInputStream();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        IOKit.transfer(in, out);
        this.stream = new ByteArrayInputStream(out.toByteArray());
        this.length = stream.available();
    }

    public MediaType getType() {
        return type;
    }

    public String getCharset() {
        return charset;
    }

    public InputStream getStream() {
        return stream;
    }

    public int getLength() {
        return length;
    }
}
