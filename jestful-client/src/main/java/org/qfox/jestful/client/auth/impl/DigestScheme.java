package org.qfox.jestful.client.auth.impl;

import org.qfox.jestful.client.auth.*;
import org.qfox.jestful.commons.Hex;
import org.qfox.jestful.commons.StringKit;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Restful;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Set;

public class DigestScheme extends RFC2617Scheme implements Scheme {
    public static final String NAME = "Digest";

    public DigestScheme() {
        this(null);
    }

    public DigestScheme(Charset charset) {
        super(charset);
    }

    private static byte[] random(int len) {
        if (len <= 0) throw new IllegalArgumentException("len <= 0");
        SecureRandom srd = new SecureRandom();
        byte[] bytes = new byte[len];
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
        if (credence == null) throw new AuthenticationException("no suitable credence provided for scope: " + scope);
        if (challenge == null) throw new AuthenticationException("can not authenticate without challenge");
        DigestChallenge dc = challenge instanceof DigestChallenge ? (DigestChallenge) challenge : new DigestChallenge(challenge);

        Charset cs = charset != null ? charset : Charset.forName(action.getHeaderEncodeCharset());
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
        MessageDigest digester = digester(algorithm.equalsIgnoreCase("MD5-sess") ? "MD5" : algorithm);

        // 计算 A1 = unq(username-value) ":" unq(realm-value) ":" password-value
        Principal principal = credence.getPrincipal();
        String username = principal != null ? principal.getName() : null;
        String password = credence.getPassword();
        String A1 = StringKit.concat(delimiter, username, realm, password);
        // 如果 algorithm == MD5-sess 那么 A1= H( unq(username-value) ":" unq(realm-value) ":" password-value ) ":" unq(nonce-value) ":" unq(cnonce-value)
        if ("MD5-sess".equalsIgnoreCase(algorithm)) {
            String hex = hex(digester.digest(StringKit.bytes(A1, cs)));
            A1 = StringKit.concat(delimiter, hex, nonce, cnonce);
        }

        // 计算A2
        String A2;
        Set<String> qualities = qops == null ? null : new HashSet<String>(qops.length);
        for (int i = 0; qops != null && i < qops.length; i++) if (qops[i] != null) qualities.add(qops[i].trim().toLowerCase());
        String qop; // 如果服务端没发送qop过来 则客户端一定不能发送qop/nc/cnonce回去
        if (qualities == null) {
            qop = null; // 服务端没有发送qop过来 客户端也不发送
            A2 = StringKit.concat(delimiter, method, uri);
        } else if (qualities.contains("auth")) {
            qop = "auth";
            A2 = StringKit.concat(delimiter, method, uri);
        } else {
            throw new AuthenticationException("none of quality option if supported: " + qualities);
        }

        String HA1 = hex(digester.digest(StringKit.bytes(A1, cs)));
        String HA2 = hex(digester.digest(StringKit.bytes(A2, cs)));

        String nc = null; // qop == null 的情况下代表服务端没发送 qop 客户端也一定不会发送qop/nc/cnonce回去 所以qop == null 情况下不需要递增nonce-count
        String text = (qop == null) ? StringKit.concat(delimiter, HA1, nonce, HA2) : StringKit.concat(delimiter, HA1, nonce, nc = dc.nc(), cnonce, qop, HA2);
        String response = hex(digester.digest(StringKit.bytes(text, Charset.forName("US-ASCII"))));
        Information information = new Information();
        information.put("username", username, true);
        information.put("realm", realm, true);
        information.put("nonce", nonce, true);
        information.put("uri", uri, true);
        information.put("response", response, true);
        information.put("algorithm", algorithm, false);
        if (opaque != null) {
            information.put("opaque", opaque, true);
        }
        // 在服务端没有发送qop过来而且algorithm == null || algorithm != MD5-sess 的情况下 所有的摘要数据的确没有用到 qop/nc/cnonce
        // 但是在 qop == null && algorithm == MD5-sess 的情况下计算A1的时候的确用了 cnonce 却又不发送回服务端那它怎么算出客户端一样的response? 不过无论如何RFC2617是这样规定
        if (qop != null) {
            information.put("qop", qop, false);
            information.put("nc", nc, false);
            information.put("cnonce", cnonce, true);
        }

        String authorization = StringKit.concat(NAME, " ", information.toString());
        authenticate(action, challenge, authorization);
    }

    @Override
    public void success(Action action, Scope scope, Credence credence, Challenge challenge) throws AuthenticationException {

    }

    @Override
    public void failure(Action action, Scope scope, Credence credence, Challenge challenge) throws AuthenticationException {

    }

    @Override
    public DigestChallenge analyze(Action action, boolean thrown, Object result, Exception exception) {
        Challenge challenge = super.analyze(action, thrown, result, exception);
        return challenge instanceof DigestChallenge ? (DigestChallenge) challenge : new DigestChallenge(challenge);
    }

    private MessageDigest digester(String algorithm) throws AuthenticationException {
        if (algorithm == null) throw new IllegalArgumentException("algorithm == null");
        try {
            return MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new AuthenticationException("unsupported message digest algorithm: " + algorithm);
        }
    }
}
