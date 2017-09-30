package org.qfox.jestful.client.nio.filter;

import org.qfox.jestful.core.exception.JestfulException;

/**
 * Created by yangchangpei on 17/9/30.
 */
public class DuplicateFiltrateException extends JestfulException {
    private static final long serialVersionUID = 1095583275038415037L;

    private final Iterable<NioFilter> filters;

    public DuplicateFiltrateException(Iterable<NioFilter> filters) {
        this.filters = filters;
    }

    public DuplicateFiltrateException(String message, Throwable cause, Iterable<NioFilter> filters) {
        super(message, cause);
        this.filters = filters;
    }

    public DuplicateFiltrateException(String message, Iterable<NioFilter> filters) {
        super(message);
        this.filters = filters;
    }

    public DuplicateFiltrateException(Throwable cause, Iterable<NioFilter> filters) {
        super(cause);
        this.filters = filters;
    }

    public Iterable<NioFilter> getFilters() {
        return filters;
    }
}
