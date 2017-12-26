package org.qfox.jestful.client.exception;

import org.qfox.jestful.core.exception.JestfulException;

public class NotAllowedTypeException extends JestfulException {
    private static final long serialVersionUID = 5819410946497810907L;

    public NotAllowedTypeException() {
    }

    public NotAllowedTypeException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotAllowedTypeException(String message) {
        super(message);
    }

    public NotAllowedTypeException(Throwable cause) {
        super(cause);
    }
}
