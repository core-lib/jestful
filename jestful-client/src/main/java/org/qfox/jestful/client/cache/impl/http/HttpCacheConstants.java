package org.qfox.jestful.client.cache.impl.http;

public interface HttpCacheConstants {
    String CACHE_CONTROL = "Cache-Control";
    String AGE = "Age";
    String LAST_MODIFIED = "Last-Modified";
    String E_TAG = "ETag";
    String IF_MODIFIED_SINCE = "If-Modified-Since";
    String IF_NONE_MATCH = "If-None-Match";
    String CACHE_TIME = "Request-Time";

    String PUBLIC = "public";
    String PRIVATE = "private";
    String NO_CACHE = "no-cache";
    String NO_STORE = "no-store";
    String NO_TRANSFORM = "no-transform";
    String MUST_REVALIDATE = "must-revalidate";
    String PROXY_REVALIDATE = "proxy-revalidate";
    String MAX_AGE = "max-age";
    String SHARED_MAX_AGE = "s-maxage";

    String MAX_STALE = "max-stale";
    String MIN_FRESH = "min-fresh";
    String ONLY_IF_CACHED = "only-if-cached";

}
