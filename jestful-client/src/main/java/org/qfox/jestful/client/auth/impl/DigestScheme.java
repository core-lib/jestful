package org.qfox.jestful.client.auth.impl;

import org.qfox.jestful.client.auth.Challenge;
import org.qfox.jestful.client.auth.Credence;
import org.qfox.jestful.client.auth.Scheme;
import org.qfox.jestful.client.auth.Scope;
import org.qfox.jestful.core.Action;

public class DigestScheme extends RFC2617Scheme implements Scheme {
    public static final String NAME = "Digest";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void authenticate(Action action, Scope scope, Credence credence, Challenge challenge) {

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
}
