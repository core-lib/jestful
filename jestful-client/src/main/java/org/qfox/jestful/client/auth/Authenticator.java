package org.qfox.jestful.client.auth;

import org.qfox.jestful.core.Action;

import java.io.Serializable;

public class Authenticator implements Serializable {
    private static final long serialVersionUID = 6971103658173832157L;

    private Status status = Status.UNCHALLENGED;
    private Scheme scheme;
    private Scope scope;
    private Credence credence;
    private Challenge challenge;

    public Authenticator(Scheme scheme, Scope scope, Credence credence, Challenge challenge) {
        this.scheme = scheme;
        this.scope = scope;
        this.credence = credence;
        this.challenge = challenge;
    }

    public void authenticate(Action action) {
        scheme.authenticate(action, scope, credence, challenge);
    }

    public synchronized void update(Scheme scheme, Scope scope, Credence credence, Challenge challenge) {
        setScheme(scheme);
        setScope(scope);
        setCredence(credence);
        setChallenge(challenge);
    }

    public synchronized void shift(Status status) {
        setStatus(status);
    }

    public Status getStatus() {
        return status;
    }

    public synchronized void setStatus(Status status) {
        this.status = status;
    }

    public Scheme getScheme() {
        return scheme;
    }

    public synchronized void setScheme(Scheme scheme) {
        this.scheme = scheme;
    }

    public Scope getScope() {
        return scope;
    }

    public synchronized void setScope(Scope scope) {
        this.scope = scope;
    }

    public Credence getCredence() {
        return credence;
    }

    public synchronized void setCredence(Credence credence) {
        this.credence = credence;
    }

    public Challenge getChallenge() {
        return challenge;
    }

    public synchronized void setChallenge(Challenge challenge) {
        this.challenge = challenge;
    }

}
