package org.qfox.jestful.client.auth;

import org.qfox.jestful.client.Client;
import org.qfox.jestful.client.Promise;
import org.qfox.jestful.client.scheduler.Callback;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Actor;

/**
 * Created by Payne on 2017/10/21.
 */
public class Authorization implements Actor {
    private CredenceProvider credenceProvider;
    private StateStorage stateStorage;
    private SchemeRegistry schemeRegistry;
    private ScopeResolver scopeResolver;
    private int maxCount = 3;

    public Authorization() {
    }

    @Override
    public Object react(Action action) throws Exception {
        Authenticator authenticator = (Authenticator) action.getExtra().get(Authenticator.class);
        // 如果存在认证器则表示该请求为认证重试请求
        if (authenticator != null) {
            authenticator.authenticate(action);
        }
        // 否则如果这是一个新请求
        else {
            Host host = new Host(action.getProtocol(), action.getHostname(), action.getPort());
            State state = stateStorage.get(host);
            authenticator = state != null ? state.getCurrent() : null;
            if (authenticator != null) authenticator.authenticate(action);
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

            // 遍历所有认证方案匹配出可以处理该结果的认证方案 匹配不到即认为没有匹配方案或者服务端没有要求认证
            Scheme scheme = schemeRegistry.matches(action, thrown, result, exception);
            if (scheme != null) {
                // 方案分析出服务端发起的认证挑战
                Challenge challenge = scheme.analyze(action, thrown, result, exception);
                // 构建授权范围
                Scope scope = new Scope(scheme.getName(), challenge.getRealm(), action.getHostname(), action.getPort());
                // 找到对应的用户凭证
                Credence credence = credenceProvider.getCredence(scope);
                // 构建主机对象
                Host host = new Host(action.getProtocol(), action.getHostname(), action.getPort());
                // 获取主机的认证状态
                State state = stateStorage.get(host);
                // 避免并发时候的状态覆盖保存问题
                if (state == null) state = stateStorage.put(host, new State(host));
                // 获取该域的认证选项
                Authenticator authenticator = state.get(challenge.getRealm());
                // 避免并发时候的状态覆盖保存问题
                if (authenticator == null) authenticator = state.put(challenge.getRealm(), new Authenticator(scheme, scope, credence, challenge));
                // 更新认证参数
                authenticator.update(scheme, scope, credence, challenge);
                // 切换认证状态
                authenticator.shift(Status.CHALLENGED);
                // 获取重试认证次数
                Integer count = (Integer) action.getExtra().get(this.getClass());
                // 为空则表示还没有认证过即为0次
                if (count == null) count = 0;
                // 如果小于最大认证次数则重试认证
                if (count < maxCount) {
                    return client().invoker()
                            .setProtocol(action.getProtocol())
                            .setHostname(action.getHostname())
                            .setPort(action.getPort())
                            .setRoute(action.getRoute())
                            .setResource(action.getResource())
                            .setMapping(action.getMapping())
                            .setParameters(action.getParameters())
                            .setForePlugins(action.getForePlugins())
                            .setBackPlugins(action.getBackPlugins())
                            .addExtra(this.getClass(), count + 1)
                            .addExtra(Authenticator.class, authenticator)
                            .promise()
                            .acquire();
                }
                // 超过最大认证次数认证失败
                else {
                    authenticator.shift(Status.UNAUTHENTICATED);
                }
            }
            // 认证通过
            else {
                Authenticator authenticator = (Authenticator) action.getExtra().get(Authenticator.class);
                if (authenticator != null) {
                    authenticator.shift(Status.AUTHENTICATED);
                    Host host = new Host(action.getProtocol(), action.getHostname(), action.getPort());
                    State state = stateStorage.get(host);
                    if (state != null) state.setCurrent(authenticator);
                }
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

    public ScopeResolver getScopeResolver() {
        return scopeResolver;
    }

    public void setScopeResolver(ScopeResolver scopeResolver) {
        this.scopeResolver = scopeResolver;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }
}
