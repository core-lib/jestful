package org.qfox.jestful.client.auth.impl;

import org.qfox.jestful.client.auth.*;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.exception.StatusException;

import java.nio.charset.Charset;

public abstract class RFC2617Scheme implements Scheme {
    protected final Charset charset;

    protected RFC2617Scheme(Charset charset) {
        this.charset = charset;
    }

    @Override
    public boolean matches(Action action, boolean thrown, Object result, Exception exception) {
        if (exception instanceof StatusException) {
            StatusException statusException = (StatusException) exception;
            String[] authenticates;
            switch (statusException.getStatus()) {
                case 401:
                    authenticates = action.getResponse().getResponseHeaders("WWW-Authenticate");
                    break;
                case 407:
                    authenticates = action.getResponse().getResponseHeaders("Proxy-Authenticate");
                    break;
                default:
                    return false;
            }
            return matches(authenticates);
        }
        return false;
    }

    /**
     * 服务端有可能会发送多个可支持的认证方式, 只要其中有一个方式是该方案支持的就返回{@code true} 否则返回{@code false}
     *
     * @param authenticates 认证方式数组
     * @return 只要其中有一个方式是该方案支持的就返回{@code true} 否则返回{@code false}
     */
    protected abstract boolean matches(String[] authenticates);

    @Override
    public Challenge analyze(Action action, boolean thrown, Object result, Exception exception) {
        String name = getName();
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
            if (authenticate == null) return new Challenge(provoker, name, null, new Information());
            // 获取第一个空格的下标
            int index = authenticate.indexOf(' ');
            // 空格前面的是认证方式的算法
            String scheme = index > 0 ? authenticate.substring(0, index).trim() : null;
            // 是否为Basic算法
            if (name.equalsIgnoreCase(scheme)) {
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
                return new Challenge(provoker, name, realm, information);
            }
        }
        throw new IllegalArgumentException("unsupported challenge analysis");
    }

    protected void authenticate(Action action, Challenge challenge, String authorization) {
        switch (challenge.getProvoker()) {
            case PROXY:
                action.getRequest().setRequestHeader("Proxy-Authorization", authorization);
                break;
            case TARGET:
                action.getRequest().setRequestHeader("Authorization", authorization);
                break;
        }
    }

    public Charset getCharset() {
        return charset;
    }
}
