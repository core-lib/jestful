package org.qfox.jestful.client.cache.impl.http;

import org.qfox.jestful.commons.StringKit;
import org.qfox.jestful.commons.collection.CaseInsensitiveMap;
import org.qfox.jestful.core.Response;

import java.util.Map;
import java.util.StringTokenizer;

/**
 * Cache-Control = "Cache-Control" ":" 1#cache-directive
 * cache-directive = cache-request-directive
 * | cache-response-directive
 * cache-request-directive =
 * "no-cache" ; Section 14.9.1
 * | "no-store" ; Section 14.9.2
 * | "max-age" "=" delta-seconds ; Section 14.9.3, 14.9.4
 * | "max-stale" [ "=" delta-seconds ] ; Section 14.9.3
 * | "min-fresh" "=" delta-seconds ; Section 14.9.3
 * | "no-transform" ; Section 14.9.5
 * | "only-if-cached" ; Section 14.9.4
 * | cache-extension ; Section 14.9.6
 * cache-response-directive =
 * "public" ; Section 14.9.1
 * | "private" [ "=" <"> 1#field-name <"> ] ; Section 14.9.1
 * | "no-cache" [ "=" <"> 1#field-name <"> ]; Section 14.9.1
 * | "no-store" ; Section 14.9.2
 * | "no-transform" ; Section 14.9.5
 * | "must-revalidate" ; Section 14.9.4
 * | "proxy-revalidate" ; Section 14.9.4
 * | "max-age" "=" delta-seconds ; Section 14.9.3
 * | "s-maxage" "=" delta-seconds ; Section 14.9.3
 * | cache-extension ; Section 14.9.6
 * cache-extension = token [ "=" ( token | quoted-string ) ]
 */
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

    public boolean has(String name) {
        return directive.containsKey(name);
    }

    public String get(String name) {
        return directive.get(name);
    }

    public void put(String name, String value) {
        directive.put(name, value);
    }

    public String remove(String name) {
        return directive.remove(name);
    }

    // --------------- cache-response-directive ---------------
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

    // --------------- cache-request-directive ---------------

    public boolean hasMaxStale() {
        return has(MAX_STALE);
    }

    public Long getMaxStale() {
        String value = get(MAX_STALE);
        return value != null ? Long.valueOf(value) : null;
    }

    public boolean hasMinFresh() {
        return has(MIN_FRESH);
    }

    public Long getMinFresh() {
        String value = get(MIN_FRESH);
        return value != null ? Long.valueOf(value) : null;
    }

    public boolean isOnlyIfCached() {
        return has(ONLY_IF_CACHED);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        for (final Map.Entry<String, String> entry : directive.entrySet()) {
            if (sb.length() > 0) sb.append(", ");
            final String key = entry.getKey();
            final String value = entry.getValue();
            if (StringKit.isEmpty(value)) sb.append(key);
            else sb.append(key).append("=").append(value);
        }
        return sb.toString();
    }
}
