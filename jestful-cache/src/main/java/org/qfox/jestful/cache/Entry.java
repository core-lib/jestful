package org.qfox.jestful.cache;

import java.io.Serializable;

/**
 * Created by yangchangpei on 17/9/9.
 */
public class Entry implements Serializable {
    private static final long serialVersionUID = 2126844837651952455L;

    private String key;
    private Object value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
