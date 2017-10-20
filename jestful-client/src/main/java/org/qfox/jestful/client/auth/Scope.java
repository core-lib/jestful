package org.qfox.jestful.client.auth;

import java.io.Serializable;

/**
 * Created by Payne on 2017/10/20.
 */
public class Scope implements Serializable {
    private static final long serialVersionUID = 4859511908812587316L;

    public static final String ANY_PROTOCOL = null;
    public static final String ANY_REALM = null;
    public static final String ANY_HOST = null;
    public static final int ANY_PORT = -1;

    private final String scheme;
    private final String realm;
    private final String host;
    private final int port;

    public Scope(String scheme, String realm, String host, int port) {
        this.scheme = scheme;
        this.realm = realm;
        this.host = host;
        this.port = port;
    }

    public int match(Scope scope) {
        return 0;
    }

    public String getScheme() {
        return scheme;
    }

    public String getRealm() {
        return realm;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Scope scope = (Scope) o;

        if (port != scope.port) return false;
        if (scheme != null ? !scheme.equals(scope.scheme) : scope.scheme != null) return false;
        if (realm != null ? !realm.equals(scope.realm) : scope.realm != null) return false;
        return host != null ? host.equals(scope.host) : scope.host == null;
    }

    @Override
    public int hashCode() {
        int result = scheme != null ? scheme.hashCode() : 0;
        result = 31 * result + (realm != null ? realm.hashCode() : 0);
        result = 31 * result + (host != null ? host.hashCode() : 0);
        result = 31 * result + port;
        return result;
    }

    @Override
    public String toString() {
        return "Scope{" +
                "scheme='" + scheme + '\'' +
                ", realm='" + realm + '\'' +
                ", host='" + host + '\'' +
                ", port=" + port +
                '}';
    }
}
