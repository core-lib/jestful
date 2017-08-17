package org.qfox.jestful.form;

import java.io.Serializable;

/**
 * Created by yangchangpei on 17/8/17.
 */
public class ReusableToken implements Token, Serializable {
    private static final long serialVersionUID = -9192608238744169417L;

    private String key;
    private long timeExpired;

    public ReusableToken() {
    }

    public ReusableToken(String key, long timeExpired) {
        this.key = key;
        this.timeExpired = timeExpired;
    }

    @Override
    public boolean expired() {
        return System.currentTimeMillis() > timeExpired;
    }

    ReusableToken reuse(String key, long timeExpired) {
        this.key = key;
        this.timeExpired = timeExpired;
        return this;
    }

}
