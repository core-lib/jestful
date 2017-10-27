package org.qfox.jestful.client.auth.impl;

import org.qfox.jestful.client.auth.Challenge;
import org.qfox.jestful.client.auth.Information;
import org.qfox.jestful.client.auth.Provoker;
import org.qfox.jestful.client.auth.Value;

import java.util.Formatter;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by yangchangpei on 17/10/24.
 */
class DigestChallenge extends Challenge {
    private static final long serialVersionUID = -2609321967217782649L;

    private final Challenge challenge;
    private final AtomicInteger count = new AtomicInteger(0);

    DigestChallenge(Challenge challenge) {
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

    String[] getDomains() {
        String domain = get("domain");
        if (domain == null) return null;
        StringTokenizer tokenizer = new StringTokenizer(domain);
        String[] domains = new String[tokenizer.countTokens()];
        int index = 0;
        while (tokenizer.hasMoreElements()) domains[index++] = tokenizer.nextToken();
        return domains;
    }

    String getNonce() {
        return get("nonce");
    }

    String getOpaque() {
        return get("opaque");
    }

    Boolean getStale() {
        String stale = get("stale");
        return stale == null ? null : stale.toLowerCase().equals("true");
    }

    String getAlgorithm() {
        return get("algorithm");
    }

    String[] getQops() {
        String qop = get("qop");
        if (qop == null) return null;
        StringTokenizer tokenizer = new StringTokenizer(qop, ",");
        String[] qops = new String[tokenizer.countTokens()];
        int index = 0;
        while (tokenizer.hasMoreElements()) qops[index++] = tokenizer.nextToken();
        return qops;
    }

    String nc() {
        StringBuilder sb = new StringBuilder();
        Formatter formatter = new Formatter(sb, Locale.US);
        formatter.format("%08x", (long) count.incrementAndGet());
        formatter.close();
        return sb.toString();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DigestChallenge{");
        sb.append("challenge=").append(challenge);
        sb.append(", count=").append(count);
        sb.append('}');
        return sb.toString();
    }
}
