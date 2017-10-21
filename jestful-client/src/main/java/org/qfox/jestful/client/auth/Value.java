package org.qfox.jestful.client.auth;

import java.io.Serializable;

/**
 * Created by yangchangpei on 17/10/21.
 */
public class Value implements Serializable {
    private static final long serialVersionUID = -2562866674573012958L;

    private final boolean quoted;
    private final String content;

    public Value(boolean quoted, String content) {
        if (content == null) throw new IllegalArgumentException("content must not be null");
        this.quoted = quoted;
        this.content = content;
    }

    public boolean isQuoted() {
        return quoted;
    }

    public String getContent() {
        return content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Value)) return false;

        Value value = (Value) o;

        return quoted == value.quoted && content.equals(value.content);

    }

    @Override
    public int hashCode() {
        int result = (quoted ? 1 : 0);
        result = 31 * result + content.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return quoted ? "\"" + content + "\"" : content;
    }
}
