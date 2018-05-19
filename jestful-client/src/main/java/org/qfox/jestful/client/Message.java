package org.qfox.jestful.client;

import org.qfox.jestful.commons.IOKit;
import org.qfox.jestful.core.Response;
import org.qfox.jestful.core.Status;

import java.io.Closeable;
import java.io.IOException;
import java.io.Serializable;

/**
 * Created by yangchangpei on 17/3/13.
 */
public final class Message implements Serializable, Closeable {
    private static final long serialVersionUID = -2348643625659731238L;

    private final boolean success;
    private final Status status;
    private final Header header;
    private final Entity entity;
    private final Exception exception;

    Message(Response response) throws IOException {
        this(response, null);
    }

    Message(Response response, Exception exception) throws IOException {
        this.success = exception == null;
        this.status = response.getResponseStatus();
        this.header = new Header(response);
        this.entity = new Entity(response);
        this.exception = exception;
    }

    public boolean isSuccess() {
        return success;
    }

    public Status getStatus() {
        return status;
    }

    public Header getHeader() {
        return header;
    }

    public Entity getEntity() {
        return entity;
    }

    public Exception getException() {
        return exception;
    }

    @Override
    public void close() {
        IOKit.close(entity);
    }
}
