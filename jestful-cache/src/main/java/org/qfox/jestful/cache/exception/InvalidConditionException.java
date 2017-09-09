package org.qfox.jestful.cache.exception;

/**
 * Created by yangchangpei on 17/9/9.
 */
public class InvalidConditionException extends Exception {
    private static final long serialVersionUID = -7414989060053901618L;

    public InvalidConditionException() {
    }

    public InvalidConditionException(String message) {
        super(message);
    }

    public InvalidConditionException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidConditionException(Throwable cause) {
        super(cause);
    }
}
