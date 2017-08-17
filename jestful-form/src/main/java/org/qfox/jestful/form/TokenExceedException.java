package org.qfox.jestful.form;

/**
 * Created by yangchangpei on 17/8/17.
 */
public class TokenExceedException extends Exception {
    private static final long serialVersionUID = -1591936514381021584L;

    public TokenExceedException() {
    }

    public TokenExceedException(String message) {
        super(message);
    }

    public TokenExceedException(String message, Throwable cause) {
        super(message, cause);
    }

    public TokenExceedException(Throwable cause) {
        super(cause);
    }
}
