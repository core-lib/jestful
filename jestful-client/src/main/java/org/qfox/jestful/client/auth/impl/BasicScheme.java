package org.qfox.jestful.client.auth.impl;

import org.qfox.jestful.client.auth.*;
import org.qfox.jestful.commons.Base64;
import org.qfox.jestful.commons.StringKit;
import org.qfox.jestful.core.Action;

import java.nio.charset.Charset;

public class BasicScheme extends RFC2617Scheme implements Scheme {
    public static final String NAME = "Basic";

    public BasicScheme() {
        this(null);
    }

    public BasicScheme(Charset charset) {
        super(charset);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void authenticate(Action action, Scope scope, Credence credence, Challenge challenge) throws AuthenticationException {
        if (credence == null) throw new AuthenticationException("no suitable credence provided for scope: " + scope);
        String username = credence.getPrincipal().getName();
        String password = credence.getPassword();
        String credentials = StringKit.concat(':', username, password);
        Charset cs = charset != null ? charset : Charset.forName(action.getHeaderEncodeCharset());
        byte[] bytes = StringKit.bytes(credentials, cs);
        String response = new String(Base64.encode(bytes));
        String authorization = StringKit.concat(' ', NAME, response);
        authenticate(action, challenge, authorization);
    }

    @Override
    public BasicChallenge analyze(Action action, boolean thrown, Object result, Exception exception) {
        Challenge challenge = super.analyze(action, thrown, result, exception);
        return challenge instanceof BasicChallenge ? (BasicChallenge) challenge : new BasicChallenge(challenge);
    }

    @Override
    protected boolean matches(String[] authenticates) {
        // 如果没有指定认证方式则代表采用 Basic 认证
        if (authenticates == null) return true;
        for (String authenticate : authenticates) {
            // 获取第一个空格的下标
            int index = authenticate.indexOf(' ');
            // 空格前面的是认证方式的算法
            String scheme = index > 0 ? authenticate.substring(0, index).trim() : null;
            // 是否为Basic算法
            if (NAME.equalsIgnoreCase(scheme)) return true;
        }
        return false;
    }
}
