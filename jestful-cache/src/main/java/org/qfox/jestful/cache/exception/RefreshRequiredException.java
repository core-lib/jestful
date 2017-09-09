package org.qfox.jestful.cache.exception;

/**
 * Created by yangchangpei on 17/9/9.
 */
public class RefreshRequiredException extends Exception {
    private static final long serialVersionUID = 7817230181406232328L;

    public RefreshRequiredException() {
    }

    public RefreshRequiredException(String message) {
        super(message);
    }

    public RefreshRequiredException(String message, Throwable cause) {
        super(message, cause);
    }

    public RefreshRequiredException(Throwable cause) {
        super(cause);
    }
}
