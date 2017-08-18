package org.qfox.jestful.form;

/**
 * Created by yangchangpei on 17/8/17.
 */
public class TokenMissedException extends Exception {
    private static final long serialVersionUID = -8523228185096211033L;

    private final String key;

    public TokenMissedException(String key) {
        this.key = key;
    }

    public TokenMissedException(String message, String key) {
        super(message);
        this.key = key;
    }

    public TokenMissedException(String message, Throwable cause, String key) {
        super(message, cause);
        this.key = key;
    }

    public TokenMissedException(Throwable cause, String key) {
        super(cause);
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
