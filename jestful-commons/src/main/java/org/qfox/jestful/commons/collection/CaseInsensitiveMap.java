package org.qfox.jestful.commons.collection;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

public class CaseInsensitiveMap<K extends String, V> extends LinkedHashMap<K, V> {
    private static final long serialVersionUID = 3174924502366804649L;

    private final Map<String, String> lowerCaseKeys;
    private final Locale locale;

    public CaseInsensitiveMap() {
        this((Locale) null);
    }

    public CaseInsensitiveMap(Locale locale) {
        this.lowerCaseKeys = new HashMap<String, String>();
        this.locale = locale != null ? locale : Locale.getDefault();
    }

    public CaseInsensitiveMap(int initialCapacity) {
        this(initialCapacity, null);
    }

    public CaseInsensitiveMap(int initialCapacity, Locale locale) {
        super(initialCapacity);
        this.lowerCaseKeys = new HashMap<String, String>(initialCapacity);
        this.locale = locale != null ? locale : Locale.getDefault();
    }

    public CaseInsensitiveMap(Map<? extends K, ? extends V> m) {
        this();
        this.putAll(m);
    }

    public V put(K key, V value) {
        String oldKey = this.lowerCaseKeys.put(this.convertKey(key), key);
        if (oldKey != null && !oldKey.equals(key)) {
            super.remove(oldKey);
        }
        return super.put(key, value);
    }

    public void putAll(Map<? extends K, ? extends V> map) {
        for (Entry<? extends K, ? extends V> entry : map.entrySet()) {
            this.put(entry.getKey(), entry.getValue());
        }
    }

    public boolean containsKey(Object key) {
        return key instanceof String && this.lowerCaseKeys.containsKey(this.convertKey((String) key));
    }

    public V get(Object key) {
        if (key instanceof String) {
            String caseInsensitiveKey = this.lowerCaseKeys.get(this.convertKey((String) key));
            if (caseInsensitiveKey != null) {
                return super.get(caseInsensitiveKey);
            }
        }
        return null;
    }

    public V getOrDefault(Object key, V defaultValue) {
        if (key instanceof String) {
            String caseInsensitiveKey = this.lowerCaseKeys.get(this.convertKey((String) key));
            if (caseInsensitiveKey != null) {
                return super.get(caseInsensitiveKey);
            }
        }
        return defaultValue;
    }

    public V remove(Object key) {
        if (key instanceof String) {
            String caseInsensitiveKey = this.lowerCaseKeys.remove(this.convertKey((String) key));
            if (caseInsensitiveKey != null) {
                return super.remove(caseInsensitiveKey);
            }
        }
        return null;
    }

    public void clear() {
        this.lowerCaseKeys.clear();
        super.clear();
    }

    private String convertKey(String key) {
        return key.toLowerCase(this.locale);
    }
}
