package org.qfox.jestful.client.auth;

import org.qfox.jestful.commons.collection.CaseInsensitiveMap;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by yangchangpei on 17/10/21.
 */
public class Information implements Serializable {
    private static final long serialVersionUID = 1990778024129491685L;

    private final Map<String, Value> map = new CaseInsensitiveMap<String, Value>();

    public Value get(String field) {
        return map.get(field);
    }

    public void put(String field, String content) {
        put(field, content, false);
    }

    public void put(String field, String content, boolean quoted) {
        put(field, new Value(quoted, content));
    }

    public void put(String field, Value value) {
        map.put(field, value);
    }

    public Value remove(String field) {
        return map.remove(field);
    }

    public boolean contains(String field) {
        return map.containsKey(field);
    }

    public void clear() {
        map.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Information)) return false;

        Information information = (Information) o;

        return map.equals(information.map);
    }

    @Override
    public int hashCode() {
        return map.hashCode();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Value> entry : map.entrySet()) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(entry.getKey()).append("=").append(entry.getValue());
        }
        return sb.toString();
    }
}
