package org.qfox.jestful.client.cache.impl;

import org.qfox.jestful.client.cache.KeyGenerator;

import java.net.URL;

public class DefaultKeyGenerator implements KeyGenerator {

    @Override
    public String generate(String method, URL url, String hash) {
        String key = url.getProtocol() + "/" + url.getHost() + (url.getPort() < 0 ? "" : ":" + url.getPort()) + "/" + method + "/" + url.getFile() + (hash != null ? "/" + hash : "");
        return key.replaceAll("/{2,}", "/");
    }
}
