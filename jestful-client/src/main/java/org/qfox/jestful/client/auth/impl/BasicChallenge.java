package org.qfox.jestful.client.auth.impl;

import org.qfox.jestful.client.auth.Challenge;
import org.qfox.jestful.client.auth.Information;
import org.qfox.jestful.client.auth.Provoker;

/**
 * Created by yangchangpei on 17/10/24.
 */
class BasicChallenge extends Challenge {
    private static final long serialVersionUID = 3264040454246522450L;
    private final Challenge challenge;

    BasicChallenge(Challenge challenge) {
        if (challenge == null) throw new IllegalArgumentException("challenge == null");
        this.challenge = challenge;
    }

    @Override
    public Provoker getProvoker() {
        return challenge.getProvoker();
    }

    @Override
    public String getScheme() {
        return challenge.getScheme();
    }

    @Override
    public String getRealm() {
        return challenge.getRealm();
    }

    @Override
    public Information getInformation() {
        return challenge.getInformation();
    }
}
