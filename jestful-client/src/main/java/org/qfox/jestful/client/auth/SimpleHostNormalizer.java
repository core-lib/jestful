package org.qfox.jestful.client.auth;

import org.qfox.jestful.client.exception.UnsupportedProtocolException;

public class SimpleHostNormalizer implements HostNormalizer {

    public static SimpleHostNormalizer INSTANCE = new SimpleHostNormalizer();

    @Override
    public Host normalize(Host host) {
        String protocol = host.getProtocol().toLowerCase();
        String name = host.getName().toLowerCase();
        int port = host.getPort();
        if (port > 0) {
            return new Host(protocol, name, port);
        } else if ("https".equals(protocol)) {
            port = 443;
        } else if ("http".equals(protocol)) {
            port = 80;
        } else {
            throw new UnsupportedProtocolException(host.getProtocol());
        }
        return new Host(protocol, name, port);
    }

}
