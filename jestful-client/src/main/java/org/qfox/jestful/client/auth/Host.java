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

        if (port != host.port) return false;
        if (protocol != null ? !protocol.equals(host.protocol) : host.protocol != null) return false;
        return name != null ? name.equals(host.name) : host.name == null;
    }

    @Override
    public int hashCode() {
        int result = protocol != null ? protocol.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
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
