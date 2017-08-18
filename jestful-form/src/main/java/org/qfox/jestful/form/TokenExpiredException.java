package org.qfox.jestful.form;

/**
 * Created by yangchangpei on 17/8/17.
 */
public class TokenExpiredException extends Exception {
    private static final long serialVersionUID = 7243292560207579646L;

    private final String key;
    private final long timeExpired;

    public TokenExpiredException(String key, long timeExpired) {
        this.key = key;
        this.timeExpired = timeExpired;
    }

    public TokenExpiredException(String message, String key, long timeExpired) {
        super(message);
        this.key = key;
        this.timeExpired = timeExpired;
    }

    public TokenExpiredException(String message, Throwable cause, String key, long timeExpired) {
        super(message, cause);
        this.key = key;
        this.timeExpired = timeExpired;
    }

    public TokenExpiredException(Throwable cause, String key, long timeExpired) {
        super(cause);
        this.key = key;
        this.timeExpired = timeExpired;
    }

    public String getKey() {
        return key;
    }

    public long getTimeExpired() {
        return timeExpired;
    }
}
