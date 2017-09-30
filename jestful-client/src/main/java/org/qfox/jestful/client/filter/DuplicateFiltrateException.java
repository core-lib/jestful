package org.qfox.jestful.client.filter;

import org.qfox.jestful.core.exception.JestfulException;

/**
 * Created by yangchangpei on 17/9/30.
 */
public class DuplicateFiltrateException extends JestfulException {
    private static final long serialVersionUID = 1095583275038415037L;

    private final Iterable<Filter> filters;

    public DuplicateFiltrateException(Iterable<Filter> filters) {
        this.filters = filters;
    }

    public DuplicateFiltrateException(String message, Throwable cause, Iterable<Filter> filters) {
        super(message, cause);
        this.filters = filters;
    }

    public DuplicateFiltrateException(String message, Iterable<Filter> filters) {
        super(message);
        this.filters = filters;
    }

    public DuplicateFiltrateException(Throwable cause, Iterable<Filter> filters) {
        super(cause);
        this.filters = filters;
    }

    public Iterable<Filter> getFilters() {
        return filters;
    }
}
