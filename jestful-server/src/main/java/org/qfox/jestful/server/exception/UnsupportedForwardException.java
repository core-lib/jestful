package org.qfox.jestful.server.exception;

import org.qfox.jestful.core.exception.JestfulException;

import javax.servlet.ServletContext;

public class UnsupportedForwardException extends JestfulException {
    private static final long serialVersionUID = 1571540900207362286L;

    private final ServletContext context;

    public UnsupportedForwardException(ServletContext context) {
        super("servlet context " + context + " is unsupports forward opertion");
        this.context = context;
    }

    public ServletContext getContext() {
        return context;
    }

}
