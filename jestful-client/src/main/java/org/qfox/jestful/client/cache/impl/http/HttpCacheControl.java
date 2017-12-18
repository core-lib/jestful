package org.qfox.jestful.client.cache.impl.http;

import org.qfox.jestful.commons.collection.CaseInsensitiveMap;
import org.qfox.jestful.core.Response;

import java.util.Map;
import java.util.StringTokenizer;

public class HttpCacheControl implements HttpCacheConstants {
    private final Map<String, String> directive;

    private HttpCacheControl(Map<String, String> directive) {
        this.directive = directive;
    }

    public static HttpCacheControl valueOf(Response response) {
        String value = response.getResponseHeader(CACHE_CONTROL);
        return value == null ? null : valueOf(value);
    }

    public static HttpCacheControl valueOf(String value) {
        Map<String, String> directive = new CaseInsensitiveMap<String, String>();
        StringTokenizer tokenizer = new StringTokenizer(value, ",=\"", true);
        String token = null;
        String field = null;
        while (tokenizer.hasMoreTokens()) {
            String t = tokenizer.nextToken();
            if (t.equals("=") || t.equals("\"")) continue;
            if (t.equals(",")) {
                if (token != null) {
                    directive.put(token.trim(), field != null ? field.trim() : "");
                    token = null;
                    field = null;
                }
            } else {
                if (token == null) token = t;
                else field = t;
            }
        }
        if (token != null) directive.put(token.trim(), field != null ? field.trim() : "");
        return new HttpCacheControl(directive);
    }

    public boolean has(String directive) {
        return this.directive.containsKey(directive);
    }

    public String get(String directive) {
        return this.directive.get(directive);
    }

    public boolean isPublic() {
        return has(PUBLIC);
    }

    public boolean isPrivate() {
        return has(PRIVATE);
    }

    public String getPrivate() {
        return get(PRIVATE);
    }

    public boolean isNoCache() {
        return has(NO_CACHE);
    }

    public String getNoCache() {
        return get(NO_CACHE);
    }

    public boolean isNoStore() {
        return has(NO_STORE);
    }

    public boolean isNoTransform() {
        return has(NO_TRANSFORM);
    }

    public boolean isMustRevalidate() {
        return has(MUST_REVALIDATE);
    }

    public boolean isProxyRevalidate() {
        return has(PROXY_REVALIDATE);
    }

    public boolean hasMaxAge() {
        String value = get(MAX_AGE);
        return value != null && value.matches("\\d+");
    }

    public Long getMaxAge() {
        String value = get(MAX_AGE);
        return value != null ? Long.valueOf(value) : null;
    }

    public boolean hasSharedMaxAge() {
        String value = get(SHARED_MAX_AGE);
        return value != null && value.matches("\\d+");
    }

    public Long getSharedMaxAge() {
        String value = get(SHARED_MAX_AGE);
        return value != null ? Long.valueOf(value) : null;
    }

    @Override
    public String toString() {
        return directive.toString();
    }
}
