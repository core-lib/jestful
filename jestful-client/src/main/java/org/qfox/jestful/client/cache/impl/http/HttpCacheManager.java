package org.qfox.jestful.client.cache.impl.http;

import org.qfox.jestful.client.cache.*;
import org.qfox.jestful.client.cache.impl.AtomicLongCacheStatistics;

import java.io.IOException;

public class HttpCacheManager extends AtomicLongCacheStatistics implements CacheManager, HttpCacheConstants {
    private final DataStorage dataStorage;

    public HttpCacheManager(DataStorage dataStorage) {
        if (dataStorage == null) throw new NullPointerException();
        this.dataStorage = dataStorage;
    }

    @Override
    public Cache find(String key) throws IOException {
        Data data = dataStorage.get(key);
        // 没有缓存
        if (data == null) {
            return null;
        }
        // 有缓存
        else {
            return new HttpCache(data);
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

}
