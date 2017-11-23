package org.qfox.jestful.client.retry;

import org.qfox.jestful.client.Client;
import org.qfox.jestful.client.Promise;
import org.qfox.jestful.client.scheduler.Callback;
import org.qfox.jestful.client.scheduler.CallbackAdapter;
import org.qfox.jestful.client.scheduler.Calling;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.BackPlugin;
import org.qfox.jestful.core.ForePlugin;

/**
 * Created by yangchangpei on 17/10/18.
 */
public class RetryController implements ForePlugin, BackPlugin {
    private RetryCondition retryCondition;
    private int maxCount;

    public RetryController() {
    }

    public RetryController(RetryCondition retryCondition, int maxCount) {
        this.retryCondition = retryCondition;
        this.maxCount = maxCount;
    }

    @Override
    public Object react(Action action) throws Exception {
        if (retryCondition == null || maxCount <= 0) return action.execute();
        Promise promise = (Promise) action.execute();
        return new RetryPromise(action, promise);
    }

    public RetryCondition getRetryCondition() {
        return retryCondition;
    }

    public void setRetryCondition(RetryCondition retryCondition) {
        this.retryCondition = retryCondition;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

    private class RetryPromise implements Promise {
        private final Action action;
        private final Promise promise;

        RetryPromise(Action action, Promise promise) {
            this.action = action;
            this.promise = promise;
        }

        @Override
        public Object acquire() throws Exception {
            Exception exception = null;
            Object result = null;
            try {
                result = promise.acquire();
            } catch (Exception e) {
                exception = e;
            }

            if (retryCondition.matches(action, exception != null, result, exception)) {
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
                            .setResult(action.getResult().reset())
                            .setForePlugins(action.getForePlugins())
                            .setBackPlugins(action.getBackPlugins())
                            .setExtra(this.getClass(), count + 1)
                            .promise()
                            .acquire();
                }
            }

            if (exception != null) {
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
                        if (retryCondition.matches(action, !success, result, exception)) {
                            Integer count = (Integer) action.getExtra().get(this.getClass());
                            if (count == null) count = 0;
                            if (count < maxCount) {
                                client().invoker()
                                        .setProtocol(action.getProtocol())
                                        .setHostname(action.getHostname())
                                        .setPort(action.getPort())
                                        .setRoute(action.getRoute())
                                        .setResource(action.getResource())
                                        .setMapping(action.getMapping())
                                        .setParameters(action.getParameters())
                                        .setResult(action.getResult().reset())
                                        .setForePlugins(action.getForePlugins())
                                        .setBackPlugins(action.getBackPlugins())
                                        .setExtra(this.getClass(), count + 1)
                                        .promise()
                                        .accept(callback);
                                return;
                            }
                        }
                    } catch (Exception e) {
                        callback.onFail(ex = e);
                        return; // 避免重复回调
                    } finally {
                        if (ex != null) callback.onCompleted(false, null, ex);
                    }
                    new Calling(callback, result, exception).call();
                }
            });
        }

        @Override
        public Client client() {
            return promise.client();
        }
    }
}
