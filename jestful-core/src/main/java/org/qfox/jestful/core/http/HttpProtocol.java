package org.qfox.jestful.core.http;

import org.qfox.jestful.commons.StringKit;
import org.qfox.jestful.core.Protocol;

import java.util.StringTokenizer;

public class HttpProtocol implements Protocol {
    private final static String NAME = "HTTP";
    private final String name;
    private final HttpVersion version;

    public HttpProtocol(String name, HttpVersion version) {
        if (StringKit.isDiffer(name, NAME, true)) throw new IllegalArgumentException("unexpected http protocol name:" + name);
        if (version == null) throw new IllegalArgumentException("http protocol version must not be null");
        this.name = name.toUpperCase();
        this.version = version;
    }

    public static HttpProtocol valueOf(String protocol) {
        if (StringKit.isEmpty(protocol)) throw new IllegalArgumentException("http protocol string must not be null or empty");
        try {
            StringTokenizer tokenizer = new StringTokenizer(protocol, "/");
            String name = tokenizer.nextToken();
            String version = tokenizer.nextToken();
            return new HttpProtocol(name, HttpVersion.valueOf(version));
        } catch (Exception e) {
            throw new IllegalArgumentException("malformed http protocol string:" + protocol, e);
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public HttpVersion getVersion() {
        return version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HttpProtocol that = (HttpProtocol) o;

        return name.equals(that.name) && version.equals(that.version);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + version.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return name + "/" + version;
    }
}
