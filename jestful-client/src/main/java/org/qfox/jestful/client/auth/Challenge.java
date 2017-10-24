package org.qfox.jestful.client.auth;

import java.io.Serializable;

/**
 * Created by Payne on 2017/10/20.
 */
public class Challenge implements Serializable {
    private static final long serialVersionUID = 1139030198376990893L;

    private final Provoker provoker;
    private final String scheme;
    private final String realm;
    private final Information information;

    protected Challenge() {
        this(null, null, null, null);
    }

    public Challenge(Provoker provoker, String scheme, String realm, Information information) {
        this.provoker = provoker;
        this.scheme = scheme;
        this.realm = realm;
        this.information = information;
    }

    public Provoker getProvoker() {
        return provoker;
    }

    public String getScheme() {
        return scheme;
    }

    public String getRealm() {
        return realm;
    }

    public Information getInformation() {
        return information;
    }

    @Override
    public String toString() {
        return "Challenge{" +
                "provoker=" + provoker +
                ", scheme='" + scheme + '\'' +
                ", realm='" + realm + '\'' +
                ", information=" + information +
                '}';
    }
}
