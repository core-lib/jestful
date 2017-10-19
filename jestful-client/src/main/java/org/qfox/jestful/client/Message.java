package org.qfox.jestful.client;

import org.qfox.jestful.commons.collection.CaseInsensitiveMap;
import org.qfox.jestful.core.Response;
import org.qfox.jestful.core.Status;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

/**
 * Created by yangchangpei on 17/3/13.
 */
public final class Message<T> implements Serializable {
    private static final long serialVersionUID = -2348643625659731238L;

    private final boolean success;
    private final Status status;
    private final Map<String, String> header = new CaseInsensitiveMap<String, String>();
    private final T entity;
    private final Exception exception;

    public Message(Response response, T entity) throws IOException {
        this.success = true;
        this.status = response != null ? response.getResponseStatus() : null;
        for (String key : response != null && response.getHeaderKeys() != null ? response.getHeaderKeys() : new String[0]) {
            String name = key != null ? key : "";
            String value = response != null ? response.getResponseHeader(key) : null;
            this.header.put(name, value);
        }
        this.entity = entity;
        this.exception = null;
    }

    public Message(Response response, Exception exception) throws IOException {
        this.success = false;
        this.status = response != null ? response.getResponseStatus() : null;
        for (String key : response != null && response.getHeaderKeys() != null ? response.getHeaderKeys() : new String[0]) {
            String name = key != null ? key : "";
            String value = response != null ? response.getResponseHeader(key) : null;
            this.header.put(name, value);
        }
        this.entity = null;
        this.exception = exception;
    }

    public boolean isSuccess() {
        return success;
    }

    public Status getStatus() {
        return status;
    }

    public Map<String, String> getHeader() {
        return header;
    }

    public T getEntity() {
        return entity;
    }

    public Exception getException() {
        return exception;
    }

}
