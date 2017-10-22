package org.qfox.jestful.client.auth;

import org.qfox.jestful.client.Client;
import org.qfox.jestful.client.Promise;
import org.qfox.jestful.client.exception.UnexpectedStatusException;
import org.qfox.jestful.client.scheduler.Callback;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Actor;

/**
 * Created by Payne on 2017/10/21.
 */
public class AuthManager implements Actor {
    private CredenceProvider credenceProvider;
    private AuthStorage authStorage;
    private HostNormalizer hostNormalizer;
    private SchemeFactoryRegistry schemeFactoryRegistry;

    public AuthManager() {
    }

    public AuthManager(CredenceProvider credenceProvider, AuthStorage authStorage, HostNormalizer hostNormalizer, SchemeFactoryRegistry schemeFactoryRegistry) {
        this.credenceProvider = credenceProvider;
        this.authStorage = authStorage;
        this.hostNormalizer = hostNormalizer;
        this.schemeFactoryRegistry = schemeFactoryRegistry;
    }

    @Override
    public Object react(Action action) throws Exception {
        // 构建主机 采用 protocol + hostname + port 的方式 这种方式就默认一个主机只采用一种认证模式
        Host host = new Host(action.getProtocol(), action.getHost(), action.getPort());
        // 从缓存中获取是否有已经认证过的方案 这样就必须让host进行一次格式化 例如将协议/主机名统一变小写 加上默认端口号假如没有制定端口号
        Host normalized = hostNormalizer.normalize(host);
        // 获取已经认证过的方案
        Scheme scheme = authStorage.get(normalized);
        // 如果存在则证明已经认证过该主机
        if (scheme != null) {
            // 构建授权域
            Scope scope = new Scope(scheme.getName(), Scope.ANY_REALM, normalized.getName(), normalized.getPort());
            // 获取用户凭证
            Credence credence = credenceProvider.getCredence(scope);
            // 如果存在则抢先认证
            if (credence != null) scheme.authenticate(action, credence);
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
            try {
                return promise.acquire();
            } catch (UnexpectedStatusException e) {
                // 认证失败
                switch (e.getStatus()) {
                    case 401:
                        String authenticate = action.getResponse().getResponseHeader("WWW-Authenticate");
                        if (authenticate != null) {

                        } else {

                        }
                        break;
                    case 407:

                        break;
                }
                // 无法处理
                throw e;
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

    public AuthStorage getAuthStorage() {
        return authStorage;
    }

    public void setAuthStorage(AuthStorage authStorage) {
        this.authStorage = authStorage;
    }

    public HostNormalizer getHostNormalizer() {
        return hostNormalizer;
    }

    public void setHostNormalizer(HostNormalizer hostNormalizer) {
        this.hostNormalizer = hostNormalizer;
    }

    public SchemeFactoryRegistry getSchemeFactoryRegistry() {
        return schemeFactoryRegistry;
    }

    public void setSchemeFactoryRegistry(SchemeFactoryRegistry schemeFactoryRegistry) {
        this.schemeFactoryRegistry = schemeFactoryRegistry;
    }
}
