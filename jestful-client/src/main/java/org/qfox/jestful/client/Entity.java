package org.qfox.jestful.client;

import org.qfox.jestful.commons.IOKit;
import org.qfox.jestful.core.MediaType;
import org.qfox.jestful.core.Response;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
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
        this.stream = response.isResponseSuccess() && response.getResponseInputStream() != null ? IOKit.transfer(response.getResponseInputStream()) : null;
        this.length = stream != null ? stream.available() : -1;
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
