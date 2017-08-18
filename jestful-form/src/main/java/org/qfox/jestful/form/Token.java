package org.qfox.jestful.form;

import java.io.Serializable;

/**
 * Created by yangchangpei on 17/8/17.
 */
public class Token implements Serializable {
    private static final long serialVersionUID = -9192608238744169417L;

    private String key;
    private long timeExpired;

    public Token() {
    }

    public Token(String key, long timeExpired) {
        this.key = key;
        this.timeExpired = timeExpired;
    }

    public boolean expired() {
        return System.currentTimeMillis() > timeExpired;
    }

    public Token reuse(String key, long timeExpired) {
        this.key = key;
        this.timeExpired = timeExpired;
        return this;
    }

    public String getKey() {
        return key;
    }

    public long getTimeExpired() {
        return timeExpired;
    }
}
