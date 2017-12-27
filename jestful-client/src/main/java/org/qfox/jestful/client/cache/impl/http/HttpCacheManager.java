package org.qfox.jestful.client.cache.impl.http;

import org.qfox.jestful.client.cache.*;
import org.qfox.jestful.client.cache.impl.AtomicLongCacheStatistics;
import org.qfox.jestful.client.exception.UnexpectedStatusException;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Request;
import org.qfox.jestful.core.Status;
import org.qfox.jestful.core.http.HttpStatus;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;

public class HttpCacheManager extends AtomicLongCacheStatistics implements CacheManager, HttpCacheConstants {
    private final DataStorage dataStorage;

    public HttpCacheManager(DataStorage dataStorage) {
        if (dataStorage == null) throw new NullPointerException();
        this.dataStorage = dataStorage;
    }

    @Override
    public Cache find(String key, Action action) throws IOException {
        final Request request = action.getRequest();
        final String directive = request.getRequestHeader(CACHE_CONTROL);
        final HttpCacheControl reqCacheControl = HttpCacheControl.valueOf(directive != null ? directive : "");
        // no-store 或者 no-cache 即忽略所有缓存
        if (reqCacheControl.isNoStore() || reqCacheControl.isNoCache()) return null;
        final Data data = dataStorage.get(key);
        // 没有缓存
        if (data == null) {
            // 如果用户强制回应必须来自缓存而不关心缓存是否过期并且在没有缓存的情况下返回 504 Gateway Timeout 回应
            if (reqCacheControl.isOnlyIfCached()) throw new UnexpectedStatusException(action.getURI(), action.getRestful().getMethod(), HttpStatus.GATEWAY_TIMEOUT, Collections.<String, String[]>emptyMap(), "");
            else return null;
        }
        // 有缓存
        else {
            final HttpCache cache = new HttpCache(data);
            final HttpCacheControl resCacheControl = cache.getCacheControl();
            // 如果有缓存而且用户强制only-if-cached那么无论这个缓存是否过期都是新鲜的
            if (reqCacheControl.isOnlyIfCached()) return new FreshHttpCache(cache);
            // max-age=delta-seconds 表明客户端愿接受一个这样一个响应，此响应的年龄不大于客户端请求里max-age指定时间（以秒为单位）（译注：如果大于的话，表明此响应是陈旧的）。
            // 除非max-stale缓存控制指令也包含在请求里，否则客户端是不能接收一个陈旧响应的。
            final Long reqMaxAge = reqCacheControl.getMaxAge();
            if (reqMaxAge != null) {
                final long ageResponded = cache.getAgeResponded();
                final long timeRequested = cache.getTimeRequested();
                final long ageCurrent = (System.currentTimeMillis() - timeRequested) / 1000L + ageResponded;
                final Long resMaxAge = resCacheControl.getMaxAge();
                final long theMaxAge = resMaxAge != null ? Math.min(reqMaxAge, resMaxAge) : reqMaxAge;
                // 如果大于缓存的当前年龄则认为缓存是新鲜的
                if (theMaxAge > ageCurrent) return new FreshHttpCache(cache);
                // 否则缓存已经过期了 则看 请求头里面是否有max-stale
                if (reqCacheControl.hasMaxStale()) {
                    final Long maxStale = reqCacheControl.getMaxStale();
                    // 如果max-stale存在但没有值 则表示客户端接受任意过期的缓存
                    if (maxStale == null) return new FreshHttpCache(cache);
                    // 否则接受过期时间内的请求
                    final long theStale = ageCurrent - theMaxAge;
                    // 如果过期时间可接受
                    if (maxStale > theStale) return new FreshHttpCache(cache);
                    // 如果不可接受
                    return new StaleHttpCache(cache);
                }
                // 缓存被认为是过期的也没有max-stale
                return new StaleHttpCache(cache);
            }


            return cache;
        }
    }

    @Override
    public Cache save(String key, NegotiatedRequest request, NegotiatedResponse response) throws IOException {
        HttpCacheControl directive = HttpCacheControl.valueOf(response);
        String lastModified = response.getResponseHeader(LAST_MODIFIED);
        String eTag = response.getResponseHeader(E_TAG);
        // 如果缓存指令存在no-store则无论如何都不应该缓存 或者 没有缓存指令而且也没有 Last-Modified 和 ETag
        if ((directive != null && directive.isNoStore()) || (directive == null && lastModified == null && eTag == null)) {
            return null;
        }
        // 否则该回应需要缓存
        else {
            Data data = dataStorage.alloc(key);
            return new HttpCache(data, request, response);
        }
    }

    private static class FreshHttpCache implements Cache {
        private final HttpCache cache;

        FreshHttpCache(HttpCache cache) {
            this.cache = cache;
        }

        @Override
        public boolean fresh() {
            return true;
        }

        @Override
        public boolean negotiable() {
            return cache.negotiable();
        }

        @Override
        public void negotiate(NegotiatedRequest request) {
            cache.negotiate(request);
        }

        @Override
        public boolean negotiated(NegotiatedResponse response) {
            return cache.negotiated(response);
        }

        @Override
        public Status getStatus() {
            return cache.getStatus();
        }

        @Override
        public Map<String, String[]> getHeader() {
            return cache.getHeader();
        }

        @Override
        public InputStream getBody() {
            return cache.getBody();
        }

    }

    private static class StaleHttpCache implements Cache {
        private final HttpCache cache;

        StaleHttpCache(HttpCache cache) {
            this.cache = cache;
        }

        @Override
        public boolean fresh() {
            return false;
        }

        @Override
        public boolean negotiable() {
            return cache.negotiable();
        }

        @Override
        public void negotiate(NegotiatedRequest request) {
            cache.negotiate(request);
        }

        @Override
        public boolean negotiated(NegotiatedResponse response) {
            return cache.negotiated(response);
        }

        @Override
        public Status getStatus() {
            return cache.getStatus();
        }

        @Override
        public Map<String, String[]> getHeader() {
            return cache.getHeader();
        }

        @Override
        public InputStream getBody() {
            return cache.getBody();
        }

    }

}
