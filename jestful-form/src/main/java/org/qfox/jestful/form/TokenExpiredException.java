package org.qfox.jestful.form;

/**
 * Created by yangchangpei on 17/8/17.
 */
public class TokenExpiredException extends Exception {
    private static final long serialVersionUID = 7243292560207579646L;

    private final String token;

    public TokenExpiredException(String token) {
        this.token = token;
    }

    public TokenExpiredException(String message, String token) {
        super(message);
        this.token = token;
    }

    public TokenExpiredException(String message, Throwable cause, String token) {
        super(message, cause);
        this.token = token;
    }

    public TokenExpiredException(Throwable cause, String token) {
        super(cause);
        this.token = token;
    }
}
