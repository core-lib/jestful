package org.qfox.jestful.client.auth;

public interface PortResolver {

    int resolve(String protocol, Integer port);

}
