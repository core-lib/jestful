package org.qfox.jestful.client.auth.impl;

import org.qfox.jestful.client.auth.*;
import org.qfox.jestful.commons.Hex;
import org.qfox.jestful.commons.StringKit;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Position;
import org.qfox.jestful.core.Restful;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Set;

public class DigestScheme extends RFC2617Scheme implements Scheme {
    public static final String NAME = "Digest";

    @Override
    public String getName() {
        return NAME;
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
    /*
        credentials      = "Digest" digest-response
        digest-response  = 1#( username | realm | nonce | digest-uri | response | [ algorithm ] | [cnonce] | [opaque] | [message-qop] | [nonce-count] | [auth-param] )
        username         = "username" "=" username-value
        username-value   = quoted-string
        digest-uri       = "uri" "=" digest-uri-value
        digest-uri-value = request-uri   ; As specified by HTTP/1.1
        message-qop      = "qop" "=" qop-value
        cnonce           = "cnonce" "=" cnonce-value
        cnonce-value     = nonce-value
        nonce-count      = "nc" "=" nc-value
        nc-value         = 8LHEX
        response         = "response" "=" request-digest
        request-digest   = <"> 32LHEX <">
        LHEX             = "0" | "1" | "2" | "3" | "4" | "5" | "6" | "7" | "8" | "9" | "a" | "b" | "c" | "d" | "e" | "f"
     */
    @Override
    public void authenticate(Action action, Scope scope, Credence credence, Challenge challenge) throws AuthenticationException {
        if (credence == null) return;
        if (challenge == null) return;
        DigestChallenge dc = challenge instanceof DigestChallenge ? (DigestChallenge) challenge : new DigestChallenge(challenge);

        char delimiter = ':';
        Restful restful = action.getRestful();
        String method = restful.getMethod();
        String route = action.getRoute();
        String path = action.getURI();
        String query = action.getQuery();
        String uri = StringKit.concat(
                route != null ? route : "",
                path != null ? path : "",
                query != null ? "?" : "",
                query != null ? query : ""
        );

        String realm = dc.getRealm();
        String nonce = dc.getNonce();
        String opaque = dc.getOpaque();
        String algorithm = dc.getAlgorithm();
        String[] qops = dc.getQops();
        String cnonce = nonce();

        // 获取摘要计算器 algorithm == null 即为默认的MD5
        if (algorithm == null) algorithm = "MD5";
        MessageDigest digester = digester(algorithm);

        // 计算 A1 = unq(username-value) ":" unq(realm-value) ":" password-value
        Principal principal = credence.getPrincipal();
        String username = principal != null ? principal.getName() : null;
        String password = credence.getPassword();
        String A1 = StringKit.concat(delimiter, username, realm, password);
        // 如果 algorithm == MD5-sess 那么 A1= H( unq(username-value) ":" unq(realm-value) ":" password-value ) ":" unq(nonce-value) ":" unq(cnonce-value)
        if ("MD5-sess".equalsIgnoreCase(algorithm)) {
            String hex = hex(digester.digest(StringKit.bytes(A1)));
            A1 = StringKit.concat(delimiter, hex, nonce, cnonce);
        }

        // 计算A2
        String A2;
        Set<String> qualities = qops == null ? null : new HashSet<String>(qops.length);
        for (int i = 0; qops != null && i < qops.length; i++) if (qops[i] != null) qualities.add(qops[i].trim().toLowerCase());
        String qop;
        if (qualities == null) {
            qop = "auth";
            A2 = StringKit.concat(delimiter, method, uri);
        } else if (qualities.contains("auth-int") && restful.isAcceptBody() && action.getParameters().count(Position.BODY) > 0) {
            qop = "auth-int";
            throw new IllegalStateException("under construction: " + qop);
        } else if (qualities.contains("auth")) {
            qop = "auth";
            A2 = StringKit.concat(delimiter, method, uri);
        } else {
            throw new AuthenticationException("none of quality option if supported: " + qualities);
        }

        String HA1 = hex(digester.digest(StringKit.bytes(A1)));
        String HA2 = hex(digester.digest(StringKit.bytes(A2)));

        String nc = dc.nc();
        String text = (qualities == null) ? StringKit.concat(delimiter, HA1, nonce, HA2) : StringKit.concat(delimiter, HA1, nonce, nc, cnonce, qop, HA2);
        String response = hex(digester.digest(StringKit.bytes(text)));
        Information information = new Information();
        information.put("username", username, true);
        information.put("realm", realm, true);
        information.put("nonce", nonce, true);
        information.put("uri", uri, true);
        information.put("response", response, true);
        information.put("algorithm", algorithm, false);
        information.put("opaque", opaque, true);
        information.put("qop", qop, false);
        information.put("nc", nc, false);
        information.put("cnonce", cnonce, true);

        String authorization = StringKit.concat(NAME, " ", information.toString());
        action.getRequest().setRequestHeader("Authorization", authorization);
    }

    @Override
    public DigestChallenge analyze(Action action, boolean thrown, Object result, Exception exception) {
        Challenge challenge = super.analyze(action, thrown, result, exception);
        return challenge instanceof DigestChallenge ? (DigestChallenge) challenge : new DigestChallenge(challenge);
    }

    @Override
    protected boolean matches(String authenticate) {
        // 如果没有指定认证方式则代表采用 Basic 认证 所以返回 false
        if (authenticate == null) return false;
        // 获取第一个空格的下标
        int index = authenticate.indexOf(' ');
        // 空格前面的是认证方式的算法
        String scheme = index > 0 ? authenticate.substring(0, index) : null;
        // 是否为Digest算法
        return NAME.equalsIgnoreCase(scheme);
    }

    private MessageDigest digester(String algorithm) throws AuthenticationException {
        if (algorithm == null) throw new IllegalArgumentException("algorithm == null");
        MessageDigest digest;
        if ("MD5-sess".equalsIgnoreCase(algorithm)) {
            try {
                digest = MessageDigest.getInstance("md5");
            } catch (NoSuchAlgorithmException e) {
                throw new AuthenticationException("unsupported message digest algorithm : md5");
            }
        } else {
            try {
                digest = MessageDigest.getInstance(algorithm);
            } catch (NoSuchAlgorithmException e) {
                throw new AuthenticationException("unsupported message digest algorithm : " + algorithm);
            }
        }
        return digest;
    }

    private static byte[] random(int len) {
        if (len <= 0) throw new IllegalArgumentException("len <= 0");
        SecureRandom srd = new SecureRandom();
        byte[] bytes = new byte[8];
        srd.nextBytes(bytes);
        return bytes;
    }

    private static String hex(byte[] bytes) {
        return hex(bytes, false);
    }

    private static String hex(byte[] bytes, boolean toUpperCase) {
        byte[] b = Hex.encode(bytes);
        String s = new String(b);
        return toUpperCase ? s.toUpperCase() : s;
    }

    private static String nonce() {
        return nonce(false);
    }

    private static String nonce(boolean toUpperCase) {
        byte[] bytes = random(8);
        String nonce = hex(bytes);
        return toUpperCase ? nonce.toUpperCase() : nonce;
    }
}
