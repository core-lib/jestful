package org.qfox.jestful.server.exception;

import org.qfox.jestful.core.exception.JestfulException;

public class DisallowEncodeException extends JestfulException {
    private static final long serialVersionUID = -8688709001061767090L;

    public DisallowEncodeException() {
        super();
    }

    public DisallowEncodeException(String message, Throwable cause) {
        super(message, cause);
    }

    public DisallowEncodeException(String message) {
        super(message);
    }

    public DisallowEncodeException(Throwable cause) {
        super(cause);
    }

}
