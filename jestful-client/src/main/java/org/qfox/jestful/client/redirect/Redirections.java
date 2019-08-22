package org.qfox.jestful.client.redirect;

import org.qfox.jestful.commons.collection.Enumerable;
import org.qfox.jestful.commons.collection.Enumerator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class Redirections implements Enumerable<Redirection>, Serializable {
    private static final long serialVersionUID = 2813058619270714851L;

    protected final Direction direction;
    protected final List<Redirection> redirections = new ArrayList<Redirection>();

    public Redirections(Direction direction) {
        this.direction = direction;
    }

    public int count() {
        return redirections.size();
    }

    protected void add(Redirection redirection) {
        redirections.add(redirection);
    }

    public Redirection[] toArray() {
        return redirections.toArray(new Redirection[0]);
    }

    @Override
    public Enumeration<Redirection> enumeration() {
        return new Enumerator<Redirection>(redirections);
    }

    public Direction getDirection() {
        return direction;
    }

    @Override
    public String toString() {
        return redirections.toString();
    }
}
