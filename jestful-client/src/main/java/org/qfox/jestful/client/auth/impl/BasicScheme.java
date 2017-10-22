package org.qfox.jestful.client.auth.impl;

import org.qfox.jestful.client.auth.*;
import org.qfox.jestful.commons.Base64;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.exception.StatusException;

public class BasicScheme implements Scheme {
    private static final String NAME = "Basic";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void authenticate(Action action, State state) {
        Credence credence = state.getCredence();
        String username = credence.getPrincipal().getName();
        String password = credence.getPassword();
        String authorization = NAME + " " + Base64.encode(username + ":" + password);
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
            return NAME.equalsIgnoreCase(algorithm);
        }
        return false;
    }

    @Override
    public Challenge analyze(Action action, boolean thrown, Object result, Exception exception) {
        if (exception instanceof StatusException) {
            StatusException statusException = (StatusException) exception;
            Provoker provoker;
            String authenticate;
            switch (statusException.getStatus()) {
                case 401:
                    provoker = Provoker.TARGET;
                    authenticate = action.getResponse().getResponseHeader("WWW-Authenticate");
                    break;
                case 407:
                    provoker = Provoker.PROXY;
                    authenticate = action.getResponse().getResponseHeader("Proxy-Authenticate");
                    break;
                default:
                    throw new IllegalArgumentException("unsupported challenge analysis");
            }
            if (authenticate == null) return new Challenge(provoker, NAME, null, new Information());
            // 获取第一个空格的下标
            int index = authenticate.indexOf(' ');
            // 空格前面的是认证方式的算法
            String algorithm = index > 0 ? authenticate.substring(0, index) : null;
            // 是否为Basic算法
            if (NAME.equalsIgnoreCase(algorithm)) {
                String text = authenticate.substring(index + 1).trim();
                Information information = new Information();
                String[] items = text.split("\\s*,\\s*");
                for (String item : items) {
                    String[] kv = item.split("\\s*=\\s*");
                    if (kv.length != 2) continue;
                    String field = kv[0];
                    String value = kv[1];
                    if ((value.startsWith("\"") && value.endsWith("\"")) || (value.startsWith("'") && value.endsWith("'"))) {
                        information.put(field, value.substring(1, value.length() - 1), true);
                    } else {
                        information.put(field, value);
                    }
                }
                Value value = information.get("realm");
                String realm = value != null ? value.getContent() : null;
                return new Challenge(provoker, NAME, realm, information);
            }
        }
        throw new IllegalArgumentException("unsupported challenge analysis");
    }

}
