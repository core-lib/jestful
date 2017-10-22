package org.qfox.jestful.client.auth;

import java.io.Serializable;

public class State implements Serializable {
    private static final long serialVersionUID = 6971103658173832157L;

    private Status status;
    private String scheme;
    private Scope scope;
    private Credence credence;

    public State(Status status, String scheme, Scope scope, Credence credence) {
        this.status = status;
        this.scheme = scheme;
        this.scope = scope;
        this.credence = credence;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public Scope getScope() {
        return scope;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    public Credence getCredence() {
        return credence;
    }

    public void setCredence(Credence credence) {
        this.credence = credence;
    }
}
