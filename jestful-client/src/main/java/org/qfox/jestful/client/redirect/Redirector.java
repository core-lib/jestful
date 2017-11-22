package org.qfox.jestful.client.redirect;

import org.qfox.jestful.client.Client;
import org.qfox.jestful.client.Promise;
import org.qfox.jestful.client.scheduler.Callback;
import org.qfox.jestful.client.scheduler.CallbackAdapter;
import org.qfox.jestful.client.scheduler.Calling;
import org.qfox.jestful.commons.CollectionKit;
import org.qfox.jestful.commons.Emptiable;
import org.qfox.jestful.commons.Predication;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Actor;

import java.util.Arrays;
import java.util.Collection;

/**
 * Created by yangchangpei on 17/10/31.
 */
public class Redirector implements Actor {
    private final static int MAX_COUNT = 30;
    private final static Collection<Redirect> REDIRECTS = Arrays.asList(

    );

    private int maxCount;
    private Collection<Redirect> redirects;

    public Redirector() {
        this(MAX_COUNT, REDIRECTS);
    }

    public Redirector(int maxCount) {
        this(maxCount, REDIRECTS);
    }

    public Redirector(Collection<Redirect> redirects) {
        this(MAX_COUNT, redirects);
    }

    public Redirector(int maxCount, Collection<Redirect> redirects) {
        if (maxCount <= 0) throw new IllegalArgumentException("maxCount must greater than zero");
        if (redirects == null || redirects.isEmpty()) throw new IllegalArgumentException("redirects must not be null or empty collection");
        this.maxCount = maxCount;
        this.redirects = redirects;
    }

    @Override
    public Object react(Action action) throws Exception {
        if (redirects == null || maxCount <= 0) return action.execute();
        Promise promise = (Promise) action.execute();
        return new RedirectPromise(action, promise);
    }

    private class RedirectPromise implements Promise {
        private final Action action;
        private final Promise promise;

        RedirectPromise(Action action, Promise promise) {
            this.action = action;
            this.promise = promise;
        }

        @Override
        public Object acquire() throws Exception {
            final boolean thrown;
            Object result = null;
            Exception exception = null;
            try {
                result = promise.acquire();
            } catch (Exception e) {
                exception = e;
            } finally {
                thrown = exception != null;
            }

            Predication<Redirect> predication = new RedirectPredication(action, thrown, result, exception);
            Emptiable<Redirect> emptiable = CollectionKit.any(redirects, predication);

            Redirect redirect = emptiable.isEmpty() ? null : emptiable.get();
            if (redirect != null) {
                Redirections redirections = (Redirections) action.getExtra().get(Redirections.class);
                if (redirections == null) redirections = new Redirections();
                redirections.add(action);

                // 获取重定向次数
                Integer count = (Integer) action.getExtra().get(Redirector.class);
                // 为空则表示还没有重定向过即为0次
                if (count == null) count = 0;
                // 如果小于最大重定向次数则继续重定向
                if (count < maxCount) {
                    Client.Invoker<?> invoker = redirect.construct(client(), action, thrown, result, exception);
                    invoker.addExtra(Redirections.class, redirections);
                    invoker.addExtra(Redirector.class, count + 1);
                    return invoker.promise().acquire();
                } else {
                    throw new RedirectionOverloadException(redirections);
                }
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
                        Predication<Redirect> predication = new RedirectPredication(action, exception != null, result, exception);
                        Emptiable<Redirect> emptiable = CollectionKit.any(redirects, predication);

                        Redirect redirect = emptiable.isEmpty() ? null : emptiable.get();
                        if (redirect != null) {
                            Redirections redirections = (Redirections) action.getExtra().get(Redirections.class);
                            if (redirections == null) redirections = new Redirections();
                            redirections.add(action);

                            // 获取重定向次数
                            Integer count = (Integer) action.getExtra().get(Redirector.class);
                            // 为空则表示还没有重定向过即为0次
                            if (count == null) count = 0;
                            // 如果小于最大重定向次数则继续重定向
                            if (count < maxCount) {
                                Client.Invoker<?> invoker = redirect.construct(client(), action, exception != null, result, exception);
                                invoker.addExtra(Redirections.class, redirections);
                                invoker.addExtra(Redirector.class, count + 1);
                                invoker.promise().accept(callback);
                                return; // 避免重复回调
                            } else {
                                throw new RedirectionOverloadException(redirections);
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

    private static class RedirectPredication implements Predication<Redirect> {
        private final Action action;
        private final boolean thrown;
        private final Object result;
        private final Exception exception;

        RedirectPredication(Action action, boolean thrown, Object result, Exception exception) {
            this.action = action;
            this.thrown = thrown;
            this.result = result;
            this.exception = exception;
        }

        @Override
        public boolean test(Redirect redirect) {
            return redirect.matches(action, thrown, result, exception);
        }

    }

    public int getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

    public Collection<Redirect> getRedirects() {
        return redirects;
    }

    public void setRedirects(Collection<Redirect> redirects) {
        this.redirects = redirects;
    }

}
