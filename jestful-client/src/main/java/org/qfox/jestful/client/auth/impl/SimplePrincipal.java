package org.qfox.jestful.client.auth.impl;

import org.qfox.jestful.client.auth.Principal;

import java.io.Serializable;

/**
 * Created by yangchangpei on 17/10/21.
 */
public class SimplePrincipal implements Principal, Serializable {
    private static final long serialVersionUID = 5472790914890641208L;

    private final String name;

    public SimplePrincipal(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SimplePrincipal)) return false;

        SimplePrincipal that = (SimplePrincipal) o;

        return name != null ? name.equals(that.name) : that.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return name;
    }
}
