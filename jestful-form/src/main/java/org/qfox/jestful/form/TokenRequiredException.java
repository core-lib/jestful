package org.qfox.jestful.form;

/**
 * Created by yangchangpei on 17/8/23.
 */
public class TokenRequiredException extends Exception {
    private static final long serialVersionUID = -1509362005799287221L;

    public TokenRequiredException() {
    }

    public TokenRequiredException(String message) {
        super(message);
    }

    public TokenRequiredException(String message, Throwable cause) {
        super(message, cause);
    }

    public TokenRequiredException(Throwable cause) {
        super(cause);
    }
}
