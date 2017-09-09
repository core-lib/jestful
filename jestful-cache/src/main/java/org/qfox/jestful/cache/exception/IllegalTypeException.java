package org.qfox.jestful.cache.exception;

/**
 * Created by yangchangpei on 17/9/9.
 */
public class IllegalTypeException extends Exception {
    private static final long serialVersionUID = 1879442256597583868L;

    public IllegalTypeException() {
    }

    public IllegalTypeException(String message) {
        super(message);
    }

    public IllegalTypeException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalTypeException(Throwable cause) {
        super(cause);
    }
}
