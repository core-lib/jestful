package org.qfox.jestful.client.auth;

import org.qfox.jestful.client.Client;
import org.qfox.jestful.client.Promise;
import org.qfox.jestful.client.scheduler.Call;
import org.qfox.jestful.client.scheduler.Callback;
import org.qfox.jestful.client.scheduler.CallbackAdapter;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Actor;

/**
 * Created by Payne on 2017/10/21.
 */
public class Authenticator implements Actor {
    private CredenceProvider credenceProvider;
    private StateStorage stateStorage;
    private SchemeRegistry schemeRegistry;
    private int maxCount = 3;

    @Override
    public Object react(Action action) throws Exception {
        Authentication authentication = (Authentication) action.getExtra().get(Authentication.class);
        // 如果存在认证器则表示该请求为认证重试请求
        if (authentication != null) {
            authentication.authenticate(action);
        }
        // 否则如果这是一个新请求
        else {
            Host host = new Host(action.getProtocol(), action.getHostname(), action.getPort());
            State state = stateStorage.get(host);
            Authentication target = state != null ? state.getTarget() : null;
            if (target != null) target.authenticate(action);
            Authentication proxy = state != null ? state.getProxy() : null;
            if (proxy != null) proxy.authenticate(action);
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
                // 构建授权域
                Realm realm = new Realm(challenge.getProvoker(), challenge.getRealm());
                // 获取该域的认证选项
                Authentication auth = state.get(realm);
                // 避免并发时候的状态覆盖保存问题
                if (auth == null) auth = state.put(realm, new Authentication(scheme, scope, credence, challenge));
                // 更新认证参数
                auth.update(scheme, scope, credence, challenge);
                // 切换认证状态
                auth.shift(Status.CHALLENGED);
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
                            .addExtra(Authentication.class, auth)
                            .promise()
                            .acquire();
                }
                // 超过最大认证次数认证失败
                else {
                    failure(auth);
                }
            }
            // 认证通过
            else {
                Authentication auth = (Authentication) action.getExtra().get(Authentication.class);
                if (auth != null) success(auth);
            }

            if (thrown) {
                throw exception;
            } else {
                return result;
            }
        }

        @Override
        public void accept(final Callback<Object> callback) {
            promise.accept(new CallbackAdapter<Object>() {
                @Override
                public void onCompleted(boolean success, Object result, Exception exception) {
                    Exception ex = null;
                    try {
                        // 遍历所有认证方案匹配出可以处理该结果的认证方案 匹配不到即认为没有匹配方案或者服务端没有要求认证
                        Scheme scheme = schemeRegistry.matches(action, exception != null, result, exception);
                        if (scheme != null) {
                            // 方案分析出服务端发起的认证挑战
                            Challenge challenge = scheme.analyze(action, exception != null, result, exception);
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
                            // 构建授权域
                            Realm realm = new Realm(challenge.getProvoker(), challenge.getRealm());
                            // 获取该域的认证选项
                            Authentication auth = state.get(realm);
                            // 避免并发时候的状态覆盖保存问题
                            if (auth == null) auth = state.put(realm, new Authentication(scheme, scope, credence, challenge));
                            // 更新认证参数
                            auth.update(scheme, scope, credence, challenge);
                            // 切换认证状态
                            auth.shift(Status.CHALLENGED);
                            // 获取重试认证次数
                            Integer count = (Integer) action.getExtra().get(this.getClass());
                            // 为空则表示还没有认证过即为0次
                            if (count == null) count = 0;
                            // 如果小于最大认证次数则重试认证
                            if (count < maxCount) {
                                client().invoker()
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
                                        .addExtra(Authentication.class, auth)
                                        .promise()
                                        .accept(callback);
                                return;
                            }
                            // 超过最大认证次数认证失败
                            else {
                                failure(auth);
                            }
                        }
                        // 认证通过
                        else {
                            Authentication auth = (Authentication) action.getExtra().get(Authentication.class);
                            if (auth != null) success(auth);
                        }
                    } catch (Exception e) {
                        callback.onFail(ex = e);
                        return; // 避免重复回调
                    } finally {
                        if (ex != null) callback.onCompleted(false, null, ex);
                    }
                    new Call(callback, result, exception).call();
                }
            });
        }

        @Override
        public Client client() {
            return promise.client();
        }

        private void success(Authentication auth) {
            if (auth == null) throw new NullPointerException();
            auth.shift(Status.AUTHENTICATED);
            Host host = new Host(action.getProtocol(), action.getHostname(), action.getPort());
            State state = stateStorage.get(host);
            if (state == null) return;
            Challenge challenge = auth.getChallenge();
            if (challenge == null) return;
            Provoker provoker = challenge.getProvoker();
            if (provoker == null) return;
            switch (provoker) {
                case PROXY:
                    state.setProxy(auth);
                    break;
                case TARGET:
                    state.setTarget(auth);
                    break;
            }
        }

        private void failure(Authentication auth) {
            auth.shift(Status.UNAUTHENTICATED);
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
