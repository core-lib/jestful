package org.qfox.jestful.server.exception;

import org.qfox.jestful.core.exception.JestfulException;

public class UnknownContextException extends JestfulException {
    private static final long serialVersionUID = -3775929998573501333L;

    private final String context;

    public UnknownContextException(String context) {
        super("unknown servlet context " + context);
        this.context = context;
    }

    public String getContext() {
        return context;
    }

}
