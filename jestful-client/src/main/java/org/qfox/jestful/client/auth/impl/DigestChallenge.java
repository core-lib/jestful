package org.qfox.jestful.client.auth.impl;

import org.qfox.jestful.client.auth.Challenge;
import org.qfox.jestful.client.auth.Information;
import org.qfox.jestful.client.auth.Provoker;
import org.qfox.jestful.client.auth.Value;

import java.util.StringTokenizer;

/**
 * Created by yangchangpei on 17/10/24.
 */
public class DigestChallenge extends Challenge {
    private static final long serialVersionUID = -2609321967217782649L;

    private final Challenge challenge;

    public DigestChallenge(Challenge challenge) {
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

    /*
        challenge         = "Digest" digest-challenge
        digest-challenge  = 1#( realm | [ domain ] | nonce | [opaque] | [stale] | [algorithm] | [qop-options] | [auth-param] )
        domain            = "domain" "=" <"> URI ( 1*SP URI ) <">
        URI               = absoluteURI | abs_path
        nonce             = "nonce" "=" nonce-value
        nonce-value       = quoted-string
        opaque            = "opaque" "=" quoted-string
        stale             = "stale" "=" ( "true" | "false" )
        algorithm         = "algorithm" "=" ( "MD5" | "MD5-sess" | token )
        qop-options       = "qop" "=" <"> 1#qop-value <">
        qop-value         = "auth" | "auth-int" | token
     */
    private String get(String field) {
        if (field == null) return null;
        else field = field.toLowerCase();
        Information information = challenge.getInformation();
        Value value = information != null ? information.get(field) : null;
        return value != null ? value.getContent() : null;
    }

    public String[] getDomains() {
        String domain = get("domain");
        if (domain == null) return null;
        StringTokenizer tokenizer = new StringTokenizer(domain);
        String[] domains = new String[tokenizer.countTokens()];
        int index = 0;
        while (tokenizer.hasMoreElements()) domains[index++] = tokenizer.nextToken();
        return domains;
    }

    public String getNonce() {
        return get("nonce");
    }

    public String getOpaque() {
        return get("opaque");
    }

    public Boolean getStale() {
        String stale = get("stale");
        return stale == null ? null : stale.toLowerCase().equals("true");
    }

    public String getAlgorithm() {
        return get("algorithm");
    }

    public String[] getQops() {
        String qop = get("qop");
        if (qop == null) return null;
        StringTokenizer tokenizer = new StringTokenizer(qop, ",");
        String[] qops = new String[tokenizer.countTokens()];
        int index = 0;
        while (tokenizer.hasMoreElements()) qops[index++] = tokenizer.nextToken();
        return qops;
    }

}
