package org.qfox.jestful.client.redirect;

import org.qfox.jestful.client.Client;
import org.qfox.jestful.client.Promise;
import org.qfox.jestful.client.redirect.exception.RedirectOverloadException;
import org.qfox.jestful.client.redirect.impl.HTTP3xxRedirects;
import org.qfox.jestful.client.redirect.impl.HashMapRecorder;
import org.qfox.jestful.client.scheduler.Callback;
import org.qfox.jestful.client.scheduler.CallbackAdapter;
import org.qfox.jestful.client.scheduler.Calling;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.BackPlugin;

/**
 * Created by yangchangpei on 17/10/31.
 */
public class Redirector implements BackPlugin {
    private int maxCount;
    private Redirects redirects;
    private Recorder recorder;

    public Redirector() {
        this(30, new HTTP3xxRedirects(), new HashMapRecorder());
    }

    public Redirector(int maxCount, Redirects redirects, Recorder recorder) {
        if (maxCount <= 0) throw new IllegalArgumentException("maxCount must greater than zero");
        if (redirects == null) throw new IllegalArgumentException("redirects must not be null");
        if (recorder == null) throw new IllegalArgumentException("recorder must not be null");
        this.maxCount = maxCount;
        this.redirects = redirects;
        this.recorder = recorder;
    }

    @Override
    public Object react(Action action) throws Exception {
        if (maxCount <= 0 || redirects == null || recorder == null) return action.execute();

        Direction direction = new Direction(action.getRestful().getMethod(), action.getURL());
        Redirection redirection = null;
        int count = 0;
        while (true) {
            if (++count > maxCount) break; // 避免死循环
            Redirection r = recorder.get(direction);
            if (r != null) direction = (redirection = r).toDirection();
            else break;
        }
        if (redirection != null) {
            Redirect redirect = redirects.get(action, redirection);
            Client client = (Client) action.getExtra().get(Client.class);
            if (redirect != null) return redirect.construct(client, action, redirection)
                    .setForePlugins(action.getForePlugins())
                    .setBackPlugins(action.getBackPlugins())
                    .promise();
        }
        Promise promise = (Promise) action.execute();
        return new RedirectPromise(action, promise);
    }

    public int getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

    public Redirects getRedirects() {
        return redirects;
    }

    public void setRedirects(Redirects redirects) {
        this.redirects = redirects;
    }

    public Recorder getRecorder() {
        return recorder;
    }

    public void setRecorder(Recorder recorder) {
        this.recorder = recorder;
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

            Redirect redirect = redirects.match(action, thrown, result, exception);
            if (redirect != null) {
                Redirections redirections = (Redirections) action.getExtra().get(Redirections.class);
                if (redirections == null) redirections = new Redirections(new Direction(action.getRestful().getMethod(), action.getURL()));

                // 获取重定向次数
                Integer count = (Integer) action.getExtra().get(Redirector.class);
                // 为空则表示还没有重定向过即为0次
                if (count == null) count = 0;
                // 如果小于最大重定向次数则继续重定向
                if (count < maxCount) {
                    Client.Invoker<?> invoker = redirect.construct(client(), action, thrown, result, exception);
                    invoker.addExtra(Redirections.class, redirections);
                    invoker.addExtra(Redirector.class, count + 1);
                    invoker.setForePlugins(action.getForePlugins());
                    invoker.setBackPlugins(action.getBackPlugins());

                    Action draft = invoker.draft();
                    Promise promise = (Promise) draft.execute();

                    Redirection redirection = new Redirection(redirect.name(), draft.getRestful().getMethod(), draft.getURL());
                    redirections.add(redirection);
                    if (redirect.permanent(action, thrown, result, exception)) {
                        Direction direction = new Direction(action.getRestful().getMethod(), action.getURL());
                        recorder.put(direction, redirection);
                    }

                    return promise.acquire();
                } else {
                    throw new RedirectOverloadException(redirections);
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
                        Redirect redirect = redirects.match(action, exception != null, result, exception);
                        if (redirect != null) {
                            Redirections redirections = (Redirections) action.getExtra().get(Redirections.class);
                            if (redirections == null) redirections = new Redirections(new Direction(action.getRestful().getMethod(), action.getURL()));

                            // 获取重定向次数
                            Integer count = (Integer) action.getExtra().get(Redirector.class);
                            // 为空则表示还没有重定向过即为0次
                            if (count == null) count = 0;
                            // 如果小于最大重定向次数则继续重定向
                            if (count < maxCount) {
                                Client.Invoker<?> invoker = redirect.construct(client(), action, exception != null, result, exception);
                                invoker.addExtra(Redirections.class, redirections);
                                invoker.addExtra(Redirector.class, count + 1);
                                invoker.setForePlugins(action.getForePlugins());
                                invoker.setBackPlugins(action.getBackPlugins());

                                Action draft = invoker.draft();
                                Promise promise = (Promise) draft.execute();

                                Redirection redirection = new Redirection(redirect.name(), draft.getRestful().getMethod(), draft.getURL());
                                redirections.add(redirection);
                                if (redirect.permanent(action, exception != null, result, exception)) {
                                    Direction direction = new Direction(action.getRestful().getMethod(), action.getURL());
                                    recorder.put(direction, redirection);
                                }

                                promise.accept(callback);
                                return; // 避免重复回调
                            } else {
                                throw new RedirectOverloadException(redirections);
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
