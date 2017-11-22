package org.qfox.jestful.client.redirect;

import org.qfox.jestful.commons.collection.Enumerable;
import org.qfox.jestful.commons.collection.Enumerator;
import org.qfox.jestful.core.Action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class Redirections implements Enumerable<Redirection>, Serializable {
    private static final long serialVersionUID = 2813058619270714851L;

    protected List<Redirection> redirections = new ArrayList<Redirection>();

    public int count() {
        return redirections.size();
    }

    protected void add(Action action) {
        Redirection redirection = new Redirection(action.getRestful().getMethod(), action.getURL());
        redirections.add(redirection);
    }

    public Redirection[] toArray() {
        return redirections.toArray(new Redirection[count()]);
    }

    @Override
    public Enumeration<Redirection> enumeration() {
        return new Enumerator<Redirection>(redirections);
    }

    @Override
    public String toString() {
        return redirections.toString();
    }
}
