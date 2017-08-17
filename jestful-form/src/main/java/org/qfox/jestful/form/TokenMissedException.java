package org.qfox.jestful.form;

/**
 * Created by yangchangpei on 17/8/17.
 */
public class TokenMissedException extends Exception {
    private static final long serialVersionUID = -8523228185096211033L;

    private final String token;

    public TokenMissedException(String token) {
        this.token = token;
    }

    public TokenMissedException(String message, String token) {
        super(message);
        this.token = token;
    }

    public TokenMissedException(String message, Throwable cause, String token) {
        super(message, cause);
        this.token = token;
    }

    public TokenMissedException(Throwable cause, String token) {
        super(cause);
        this.token = token;
    }
}
