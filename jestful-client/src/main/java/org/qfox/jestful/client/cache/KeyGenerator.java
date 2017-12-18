package org.qfox.jestful.client.cache;

import java.net.URL;

public interface KeyGenerator {

    String generate(String method, URL url, String hash);

}
