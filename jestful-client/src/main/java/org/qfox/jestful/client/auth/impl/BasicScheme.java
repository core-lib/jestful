package org.qfox.jestful.client.auth.impl;

import org.qfox.jestful.client.auth.Challenge;
import org.qfox.jestful.client.auth.Credence;
import org.qfox.jestful.client.auth.Scheme;
import org.qfox.jestful.client.auth.Scope;
import org.qfox.jestful.commons.Base64;
import org.qfox.jestful.core.Action;

public class BasicScheme extends RFC2617Scheme implements Scheme {
    public static final String NAME = "Basic";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void authenticate(Action action, Scope scope, Credence credence, Challenge challenge) {
        if (credence == null) return;
        String username = credence.getPrincipal().getName();
        String password = credence.getPassword();
        String authorization = NAME + " " + Base64.encode(username + ":" + password);
        action.getRequest().setRequestHeader("Authorization", authorization);
    }

    @Override
    public BasicChallenge analyze(Action action, boolean thrown, Object result, Exception exception) {
        Challenge challenge = super.analyze(action, thrown, result, exception);
        return challenge instanceof BasicChallenge ? (BasicChallenge) challenge : new BasicChallenge(challenge);
    }

    @Override
    protected boolean matches(String authenticate) {
        // 如果没有指定认证方式则代表采用 Basic 认证
        if (authenticate == null) return true;
        // 获取第一个空格的下标
        int index = authenticate.indexOf(' ');
        // 空格前面的是认证方式的算法
        String scheme = index > 0 ? authenticate.substring(0, index).trim() : null;
        // 是否为Basic算法
        return NAME.equalsIgnoreCase(scheme);
    }
}
