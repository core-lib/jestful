package org.qfox.jestful.client;

import org.qfox.jestful.core.Response;
import org.qfox.jestful.core.Status;

import java.io.Serializable;

/**
 * Created by yangchangpei on 17/3/13.
 */
public final class Message implements Serializable {
    private static final long serialVersionUID = -2348643625659731238L;

    private final boolean success;
    private final Status status;
    private final Header header;
    private final Entity entity;
    private final Exception exception;

    Message(Response response) {
        this(response, null);
    }

    Message(Response response, Exception ex) {
        boolean success = false;
        Status status = null;
        Header header = null;
        Entity entity = null;
        Exception exception = null;
        try {
            success = ex == null;
            status = response.getResponseStatus();
            header = new Header(response);
            entity = new Entity(response);
            exception = ex;
        } catch (Exception e) {
            success = false;
            exception = ex != null ? ex : e;
        } finally {
            this.success = success;
            this.status = status;
            this.header = header;
            this.entity = entity;
            this.exception = exception;
        }
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

}
