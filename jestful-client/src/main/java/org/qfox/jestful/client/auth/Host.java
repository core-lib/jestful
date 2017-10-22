package org.qfox.jestful.client.auth;

import java.io.Serializable;

/**
 * Created by Payne on 2017/10/20.
 */
public class Host implements Serializable {
    private static final long serialVersionUID = -4799044442174815412L;
    private final String protocol;
    private final String name;
    private final int port;

    public Host(String protocol, String name, int port) {
        if (protocol == null) throw new IllegalArgumentException("protocol == null");
        if (name == null) throw new IllegalArgumentException("name == null");
        this.protocol = protocol;
        this.name = name;
        this.port = port;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getName() {
        return name;
    }

    public int getPort() {
        return port;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Host host = (Host) o;

        return port == host.port && protocol.equals(host.protocol) && name.equals(host.name);
    }

    @Override
    public int hashCode() {
        int result = protocol.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + port;
        return result;
    }

    @Override
    public String toString() {
        return "Host{" +
                "protocol='" + protocol + '\'' +
                ", name='" + name + '\'' +
                ", port=" + port +
                '}';
    }
}
