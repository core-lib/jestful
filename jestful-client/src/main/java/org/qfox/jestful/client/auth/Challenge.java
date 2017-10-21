package org.qfox.jestful.client.auth;

import java.io.Serializable;

/**
 * Created by Payne on 2017/10/20.
 */
public class Challenge implements Serializable {
    private static final long serialVersionUID = 1139030198376990893L;

    private final Provoker provoker;
    private final String algorithm;
    private final String realm;
    private final Information information;

    public Challenge(Provoker provoker, String algorithm, String realm, Information information) {
        this.provoker = provoker;
        this.algorithm = algorithm;
        this.realm = realm;
        this.information = information;
    }

    public static Challenge valueOf(Provoker provoker, String authenticate) {

        return null;
    }

    public Provoker getProvoker() {
        return provoker;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public String getRealm() {
        return realm;
    }

    public Information getInformation() {
        return information;
    }
}
