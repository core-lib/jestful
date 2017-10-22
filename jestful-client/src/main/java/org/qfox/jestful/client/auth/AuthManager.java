package org.qfox.jestful.client.auth;

import org.qfox.jestful.client.Client;
import org.qfox.jestful.client.Promise;
import org.qfox.jestful.client.scheduler.Callback;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Actor;

/**
 * Created by Payne on 2017/10/21.
 */
public class AuthManager implements Actor {
    private CredenceProvider credenceProvider;
    private StateStorage stateStorage;
    private SchemeRegistry schemeRegistry;

    public AuthManager() {
    }

    @Override
    public Object react(Action action) throws Exception {
        // 构建主机 采用 protocol + hostname + port 的方式 这种方式就默认一个主机只采用一种认证模式
        Host host = new Host(action.getProtocol(), action.getHost(), action.getPort());
        // 获取已经认证状态
        State state = stateStorage.get(host);
        // 如果存在则证明已经认证过该主机
        if (state != null) {
            // 获取认证方案
            Scheme scheme = schemeRegistry.lookup(state.getScheme());
            if (scheme != null) scheme.authenticate(action, state);// 如果认证方案存在则认证
            else state.setStatus(Status.UNCHALLENGED);// 否则认证方案被用户撤销
        }
        // 封装自动认证的 Promise 处理认证失败的情况
        Promise promise = (Promise) action.execute();
        return new AuthPromise(action, promise);
    }

    private class AuthPromise implements Promise {
        private final Action action;
        private final Promise promise;

        AuthPromise(Action action, Promise promise) {
            this.action = action;
            this.promise = promise;
        }

        @Override
        public Object acquire() throws Exception {
            boolean thrown;
            Object result = null;
            Exception exception = null;
            try {
                result = promise.acquire();
            } catch (Exception e) {
                exception = e;
            } finally {
                thrown = exception != null;
            }

            Scheme scheme = schemeRegistry.matches(action, thrown, result, exception);
            if (scheme != null) {

            }

            if (thrown) {
                throw exception;
            } else {
                return result;
            }
        }


        @Override
        public void accept(Callback<Object> callback) {

        }

        @Override
        public Client client() {
            return promise.client();
        }
    }

    public CredenceProvider getCredenceProvider() {
        return credenceProvider;
    }

    public void setCredenceProvider(CredenceProvider credenceProvider) {
        this.credenceProvider = credenceProvider;
    }

    public StateStorage getStateStorage() {
        return stateStorage;
    }

    public void setStateStorage(StateStorage stateStorage) {
        this.stateStorage = stateStorage;
    }

    public SchemeRegistry getSchemeRegistry() {
        return schemeRegistry;
    }

    public void setSchemeRegistry(SchemeRegistry schemeRegistry) {
        this.schemeRegistry = schemeRegistry;
    }
}
