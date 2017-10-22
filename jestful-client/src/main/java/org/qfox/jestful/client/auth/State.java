package org.qfox.jestful.client.auth;

import org.qfox.jestful.core.Action;

import java.io.Serializable;

public class State implements Serializable {
    private static final long serialVersionUID = 6971103658173832157L;

    private Status status;
    private Scheme scheme;
    private Scope scope;
    private Credence credence;
    private Challenge challenge;

    public State(Status status, Scheme scheme, Scope scope, Credence credence, Challenge challenge) {
        this.status = status;
        this.scheme = scheme;
        this.scope = scope;
        this.credence = credence;
        this.challenge = challenge;
    }

    public void authenticate(Action action) {
        scheme.authenticate(action, this);
    }

    public void update(Status status) {
        this.status = status != null ? status : Status.UNCHALLENGED;
    }

    public void update(Scheme scheme, Scope scope, Credence credence, Challenge challenge) {
        this.scheme = scheme;
        this.scope = scope;
        this.credence = credence;
        this.challenge = challenge;
    }

    public void update(Status status, Scheme scheme, Scope scope, Credence credence, Challenge challenge) {
        update(status);
        update(scheme, scope, credence, challenge);
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Scheme getScheme() {
        return scheme;
    }

    public void setScheme(Scheme scheme) {
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

    public Challenge getChallenge() {
        return challenge;
    }

    public void setChallenge(Challenge challenge) {
        this.challenge = challenge;
    }

}
