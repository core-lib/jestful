package org.qfox.jestful.client.cache.impl.http;

import org.qfox.jestful.client.cache.impl.http.annotation.CacheControl;
import org.qfox.jestful.client.cache.impl.http.annotation.CacheExtension;
import org.qfox.jestful.commons.StringKit;
import org.qfox.jestful.commons.collection.CaseInsensitiveMap;
import org.qfox.jestful.core.*;

import java.util.Map;

public class HttpCacheConstantDirector implements Actor {

    @Override
    public Object react(Action action) throws Exception {
        final Resource r = action.getResource();
        final Mapping m = action.getMapping();
        final Class<CacheControl> c = CacheControl.class;
        final CacheControl directive = m.isAnnotationPresent(c) ? m.getAnnotation(c) : r.isAnnotationPresent(c) ? r.getAnnotation(c) : null;
        if (directive == null) return action.execute();
        final Map<String, String> map = new CaseInsensitiveMap<String, String>();
        if (directive.noCache()) map.put("no-cache", "");
        if (directive.noStore()) map.put("no-store", "");
        if (directive.maxAge() >= 0) map.put("max-age", String.valueOf(directive.maxAge()));
        if (directive.maxStale() >= 0) map.put("max-stale", directive.maxStale() == 0 ? "" : String.valueOf(directive.maxStale()));
        if (directive.minFresh() >= 0) map.put("min-fresh", String.valueOf(directive.minFresh()));
        if (directive.noTransform()) map.put("no-transform", "");
        if (directive.onlyIfCached()) map.put("only-if-cached", "");
        final CacheExtension[] extensions = directive.cacheExtensions();
        for (CacheExtension extension : extensions) {
            final String key = extension.key();
            final String value = extension.value();
            if (StringKit.isBlank(key) && StringKit.isBlank(value)) continue;
            if (StringKit.isBlank(key)) map.put(value.trim(), "");
            else map.put(key.trim(), extension.quoted() ? "\"" + value.trim() + "\"" : value.trim());
        }
        final StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (sb.length() > 0) sb.append(", ");
            final String key = entry.getKey();
            final String value = entry.getValue();
            sb.append(key);
            if (StringKit.isBlank(value)) continue;
            sb.append("=").append(value);
        }
        final Request request = action.getRequest();
        request.setRequestHeader("Cache-Control", sb.toString());
        return action.execute();
    }

}
