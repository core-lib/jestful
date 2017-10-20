package org.qfox.jestful.client.auth;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by Payne on 2017/10/20.
 */
public class Challenge implements Serializable {
    private static final long serialVersionUID = 1139030198376990893L;

    private final Provoker provoker;
    private final String algorithm;
    private final String realm;
    private final Map<String, String> properties;

    public Challenge(Provoker provoker, String algorithm, String realm, Map<String, String> properties) {
        this.provoker = provoker;
        this.algorithm = algorithm;
        this.realm = realm;
        this.properties = properties;
    }

    public static Challenge valueOf(String authenticate) {
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

    public Map<String, String> getProperties() {
        return properties;
    }
}
