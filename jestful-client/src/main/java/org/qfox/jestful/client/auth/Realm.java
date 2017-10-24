package org.qfox.jestful.client.auth;

import java.io.Serializable;

public class Realm implements Serializable {
    private static final long serialVersionUID = 3776664496625441852L;

    private final Provoker provoker;
    private final String name;

    public Realm(Provoker provoker, String name) {
        this.provoker = provoker;
        this.name = name;
    }

    public Provoker getProvoker() {
        return provoker;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Realm realm = (Realm) o;
        return provoker == realm.provoker && (name != null ? name.equals(realm.name) : realm.name == null);
    }

    @Override
    public int hashCode() {
        int result = provoker != null ? provoker.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Realm{" +
                "provoker=" + provoker +
                ", name='" + name + '\'' +
                '}';
    }
}
