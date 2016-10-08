package org.qfox.jestful.server.exception;

import org.qfox.jestful.core.exception.JestfulException;

import javax.servlet.DispatcherType;

/**
 * Created by yangchangpei on 16/10/8.
 */
public class UnsupportedDispatchException extends JestfulException {
    private final DispatcherType dispatcherType;

    public UnsupportedDispatchException(DispatcherType dispatcherType) {
        this.dispatcherType = dispatcherType;
    }

    public DispatcherType getDispatcherType() {
        return dispatcherType;
    }
}
