package org.qfox.jestful.client.auth.impl;

import org.qfox.jestful.client.auth.Credence;
import org.qfox.jestful.client.auth.Scheme;
import org.qfox.jestful.client.auth.State;
import org.qfox.jestful.commons.Base64;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.exception.StatusException;

public class BasicScheme implements Scheme {

    @Override
    public String getName() {
        return "BASIC";
    }

    @Override
    public void authenticate(Action action, State state) {
        Credence credence = state.getCredence();
        String username = credence.getPrincipal().getName();
        String password = credence.getPassword();
        String authorization = "Basic " + Base64.encode(username + ":" + password);
        action.getRequest().setRequestHeader("Authorization", authorization);
    }

    @Override
    public boolean matches(Action action, boolean thrown, Object result, Exception exception) {
        if (exception instanceof StatusException) {
            StatusException statusException = (StatusException) exception;
            String authenticate;
            switch (statusException.getStatus()) {
                case 401:
                    authenticate = action.getResponse().getResponseHeader("WWW-Authenticate");
                    break;
                case 407:
                    authenticate = action.getResponse().getResponseHeader("Proxy-Authenticate");
                    break;
                default:
                    return false;
            }
            // 如果没有指定认证方式则代表采用 Basic 认证
            if (authenticate == null) return true;
            // 获取第一个空格的下标
            int index = authenticate.indexOf(' ');
            // 空格前面的是认证方式的算法
            String algorithm = index > 0 ? authenticate.substring(0, index) : null;
            // 是否为Basic算法
            return "Basic".equalsIgnoreCase(algorithm);
        }
        return false;
    }

}
