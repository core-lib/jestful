package org.qfox.jestful.client.auth.impl;

import org.qfox.jestful.client.auth.Challenge;
import org.qfox.jestful.client.auth.Credence;
import org.qfox.jestful.client.auth.Scheme;
import org.qfox.jestful.client.auth.Scope;
import org.qfox.jestful.client.auth.exception.AuthenticationException;
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
    public void success(Action action, Scope scope, Credence credence, Challenge challenge) throws AuthenticationException {

    }

    @Override
    public void failure(Action action, Scope scope, Credence credence, Challenge challenge) throws AuthenticationException {

    }

    @Override
    public BasicChallenge analyze(Action action, boolean thrown, Object result, Exception exception) {
        Challenge challenge = super.analyze(action, thrown, result, exception);
        return challenge instanceof BasicChallenge ? (BasicChallenge) challenge : new BasicChallenge(challenge);
    }

    @Override
    protected boolean matches(String[] authenticates) {
        return authenticates == null || super.matches(authenticates);
    }
}
