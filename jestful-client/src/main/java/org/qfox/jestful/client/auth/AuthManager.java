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
    private int maxCount = 3;

    public AuthManager() {
    }

    @Override
    public Object react(Action action) throws Exception {
        // 构建主机 采用 protocol + hostname + port 的方式 这种方式就默认一个主机只采用一种认证模式
        Host host = new Host(action.getProtocol(), action.getHostname(), action.getPort());
        // 获取已经认证状态
        State state = stateStorage.get(host);
        // 如果存在则证明已经认证过该主机然后继续认证
        if (state != null) state.authenticate(action);
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
                // 如果用户提供了对应的凭证
                if (credence != null) {
                    Host host = new Host(action.getProtocol(), action.getHostname(), action.getPort());
                    State state = stateStorage.get(host);
                    if (state != null) state.update(scheme, scope, credence, challenge);
                    else stateStorage.put(host, state = new State(Status.UNCHALLENGED, scheme, scope, credence, challenge));

                    Integer count = (Integer) action.getExtra().get(this.getClass());
                    if (count == null) count = 0;
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
                                .setExtra(this.getClass(), count + 1)
                                .promise()
                                .acquire();
                    } else {
                        state.update(Status.FAILURE);
                    }
                }
            } else {
                Host host = new Host(action.getProtocol(), action.getHostname(), action.getPort());
                State state = stateStorage.get(host);
                if (state != null) state.update(Status.CHALLENGED);
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

    public int getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }
}
