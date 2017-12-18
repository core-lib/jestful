package org.qfox.jestful.client.cache.impl.http;

import org.qfox.jestful.client.cache.Cache;
import org.qfox.jestful.client.cache.Data;
import org.qfox.jestful.client.cache.NegotiatedRequest;
import org.qfox.jestful.client.cache.NegotiatedResponse;
import org.qfox.jestful.commons.IOKit;
import org.qfox.jestful.commons.StringKit;
import org.qfox.jestful.commons.collection.CaseInsensitiveMap;
import org.qfox.jestful.core.Status;
import org.qfox.jestful.core.http.HttpStatus;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

/**
 * HTTP 的缓存备份对象
 * 基本逻辑:
 * 1. 当Cache-Control中包含no-store时无论如何这次回应都不应该用作后续的缓存
 * 2. 当包含no-cache时即无论是否包含max-age都是必须和服务器验证的 即{@link HttpCache#fresh()}一直是{@code false}
 * 3. 当包含max-age而且还没有过期时即当前还是保鲜的{@link HttpCache#fresh()}是{@code true}
 * 4. 当回应头中包含 "Last-Modified" 或 "ETag"  {@link HttpCache#negotiable()} 为 {@code true} 否则为 {@code false}
 */
class HttpCache implements Cache, Data.Reader, Data.Writer, HttpCacheConstants {
    private final NegotiatedResponse response;
    private volatile Status status;
    private volatile Map<String, String[]> header;
    private volatile InputStream body;
    private volatile HttpCacheControl directive;
    private volatile long timeCached;

    HttpCache(Data data, NegotiatedResponse response) throws IOException {
        this.response = response;
        this.status = response.getResponseStatus();
        this.header = new CaseInsensitiveMap<String, String[]>();
        final String[] names = response.getHeaderKeys();
        for (String name : names) if (!StringKit.isEmpty(name)) this.header.put(name, response.getResponseHeaders(name));
        this.body = response.getResponseBodyInputStream();
        this.timeCached = System.currentTimeMillis();
        data.write(this);
    }

    HttpCache(Data data) throws IOException {
        this.response = null;
        data.read(this);
    }

    @Override
    public boolean fresh() {
        // 有缓存指令但没有包含no-cache同时有max-age且max-age未超时
        return directive != null && !directive.isNoCache() && directive.hasMaxAge() && timeCached + directive.getMaxAge() * 1000L > System.currentTimeMillis();
    }

    @Override
    public boolean negotiable() {
        return header.containsKey(LAST_MODIFIED) || header.containsKey(E_TAG);
    }

    @Override
    public void negotiate(NegotiatedRequest request) {
        String lastModified = header.containsKey(LAST_MODIFIED) ? header.get(LAST_MODIFIED)[0] : null;
        if (lastModified != null) request.setRequestHeader(IF_MODIFIED_SINCE, lastModified);
        String eTag = header.containsKey(E_TAG) ? header.get(E_TAG)[0] : null;
        if (eTag != null) request.setRequestHeader(IF_NONE_MATCH, eTag);
    }

    @Override
    public boolean negotiated(NegotiatedResponse response) {
        try {
            Status status = response.getResponseStatus();
            return status.getCode() == 304;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public Status getStatus() {
        return status;
    }

    @Override
    public Map<String, String[]> getHeader() {
        return header;
    }

    @Override
    public InputStream getBody() {
        return body;
    }

    @Override
    public void read(InputStream in) throws IOException {
        status = HttpStatus.valueOf(IOKit.readln(in));
        header = new CaseInsensitiveMap<String, String[]>();
        while (true) {
            String line = IOKit.readln(in);
            if (StringKit.isEmpty(line)) break;
            int index = line.indexOf(':');
            if (index == -1) continue;
            String key = line.substring(0, index).trim();
            String value = line.substring(index + 1).trim();
            String[] olds = header.get(key);
            if (olds == null) {
                header.put(key, new String[]{value});
            } else {
                String[] news = new String[olds.length + 1];
                System.arraycopy(olds, 0, news, 0, olds.length);
                news[olds.length] = value;
                header.put(key, news);
            }
        }
        body = in;
        directive = header.containsKey(CACHE_CONTROL) ? HttpCacheControl.valueOf(header.get(CACHE_CONTROL)[0]) : null;
        timeCached = header.containsKey(CACHE_TIME) ? Long.valueOf(header.remove(CACHE_TIME)[0]) : Long.MIN_VALUE;
    }

    @Override
    public void write(OutputStream out) throws IOException {
        // 状态行
        Status status = response.getResponseStatus();
        IOKit.writeln(status.toString(), out);
        // 协议头
        String[] names = response.getHeaderKeys();
        for (String name : names) {
            if (StringKit.isEmpty(name)) continue;
            String[] values = response.getResponseHeaders(name);
            for (String value : values) IOKit.writeln(name + ": " + value, out);
        }
        // 写入缓存时间
        IOKit.writeln(CACHE_TIME + ": " + String.valueOf(timeCached), out);
        // 空行
        IOKit.writeln("", out);
        // 回应体
        InputStream body = response.getResponseBodyInputStream();
        IOKit.transfer(body, out);
    }
}
