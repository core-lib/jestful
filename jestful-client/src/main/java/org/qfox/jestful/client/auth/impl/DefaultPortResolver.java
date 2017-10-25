package org.qfox.jestful.client.auth.impl;

import org.qfox.jestful.client.auth.PortResolver;

public class DefaultPortResolver implements PortResolver {

    @Override
    public int resolve(String protocol, Integer port) {
        if (port != null && port >= 0) return port;
        else if ("https".equalsIgnoreCase(protocol)) return 443;
        else if ("http".equalsIgnoreCase(protocol)) return 80;
        else throw new IllegalArgumentException("the default port of protocol: " + protocol + " is unknown");
    }

}
