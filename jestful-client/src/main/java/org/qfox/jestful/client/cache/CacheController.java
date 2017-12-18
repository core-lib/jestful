package org.qfox.jestful.client.cache;

import org.qfox.jestful.client.Client;
import org.qfox.jestful.client.Promise;
import org.qfox.jestful.client.cache.impl.*;
import org.qfox.jestful.client.cache.impl.http.HttpCacheManager;
import org.qfox.jestful.client.scheduler.Callback;
import org.qfox.jestful.client.scheduler.CallbackAdapter;
import org.qfox.jestful.client.scheduler.Calling;
import org.qfox.jestful.core.*;

import java.io.InputStream;
import java.net.URL;

public class CacheController implements ForePlugin, BackPlugin {
    private MsgDigester msgDigester;
    private StrEncoder strEncoder;
    private KeyGenerator keyGenerator;
    private CacheManager cacheManager;

    public CacheController() {
        this.msgDigester = new ConcurrentMsgDigester(new BasicMsgDigesterFactory("MD5"));
        this.strEncoder = new HexStrEncoder();
        this.keyGenerator = new DefaultKeyGenerator();
        this.cacheManager = new HttpCacheManager(new BasicDataStorage());
    }

    @Override
    public Object react(Action action) throws Exception {
        Promise promise = (Promise) action.execute();
        return new CachePromise(action, promise);
    }

    public MsgDigester getMsgDigester() {
        return msgDigester;
    }

    public void setMsgDigester(MsgDigester msgDigester) {
        this.msgDigester = msgDigester;
    }

    public StrEncoder getStrEncoder() {
        return strEncoder;
    }

    public void setStrEncoder(StrEncoder strEncoder) {
        this.strEncoder = strEncoder;
    }

    public KeyGenerator getKeyGenerator() {
        return keyGenerator;
    }

    public void setKeyGenerator(KeyGenerator keyGenerator) {
        this.keyGenerator = keyGenerator;
    }

    public CacheManager getCacheManager() {
        return cacheManager;
    }

    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    private class CachePromise implements Promise {
        private final Action action;
        private final Promise promise;

        CachePromise(Action action, Promise promise) {
            this.action = action;
            this.promise = promise;
        }

        @Override
        public Object acquire() throws Exception {
            // 为了适配有请求体的请求缓存 所以需要序列化请求体 并且计算请求体的hash值
            final Request srcRequest = action.getRequest();
            final CachedRequest cachedRequest = new CachedRequest(srcRequest);
            action.setRequest(cachedRequest);
            client().serialize(action);
            final InputStream in = cachedRequest.getRequestBodyInputStream();
            final String hash = in != null && in.available() > 0 ? strEncoder.encode(msgDigester.digest(in)) : null;
            final String key = keyGenerator.generate(action.getRestful().getMethod(), new URL(action.getURL()), hash);
            final Cache cache = cacheManager.find(key);
            // 没有缓存
            if (cache == null) {
                action.setRequest(srcRequest);
                return getFromServer(key);
            }
            // 如果缓存是新鲜的
            else if (cache.fresh()) {
                promise.cancel();
                return getFromCache(cache);
            }
            // 如果缓存是可协商的
            else if (cache.negotiable()) {
                NegotiatedRequest negotiatedRequest = new NegotiatedRequest(srcRequest);
                cache.negotiate(negotiatedRequest);
                action.setRequest(negotiatedRequest);
                return getFromNegotiation(key, cache);
            }
            // 存在缓存但是不是新鲜的也不能协商
            else {
                action.setRequest(srcRequest);
                return getFromServer(key);
            }
        }

        private Object getFromServer(String key) throws Exception {
            final Response srcResponse = action.getResponse();
            final NegotiatedResponse negotiatedResponse = new NegotiatedResponse(srcResponse);
            action.setResponse(negotiatedResponse);
            final Object value = promise.acquire();
            cacheManager.save(key, negotiatedResponse);
            return value;
        }

        private Object getFromCache(Cache cache) throws Exception {
            final Response srcResponse = action.getResponse();
            final CachedResponse cachedResponse = new CachedResponse(srcResponse, cache);
            action.setResponse(cachedResponse);
            client().deserialize(action);
            return action.getResult().getBody().getValue();
        }

        private Object getFromNegotiation(String key, Cache cache) throws Exception {
            final Response srcResponse = action.getResponse();
            final NegotiatedResponse negotiatedResponse = new NegotiatedResponse(srcResponse);
            action.setResponse(negotiatedResponse);
            try {
                final Object value = promise.acquire();
                cacheManager.save(key, negotiatedResponse);
                return value;
            } catch (Exception e) {
                if (cache.negotiated(negotiatedResponse)) return getFromCache(cache);
                else throw e;
            }
        }

        @Override
        public void accept(Callback<Object> callback) {
            try {
                // 为了适配有请求体的请求缓存 所以需要序列化请求体 并且计算请求体的hash值
                final Request srcRequest = action.getRequest();
                final CachedRequest cachedRequest = new CachedRequest(srcRequest);
                action.setRequest(cachedRequest);
                client().serialize(action);
                final InputStream in = cachedRequest.getRequestBodyInputStream();
                final String hash = in != null && in.available() > 0 ? strEncoder.encode(msgDigester.digest(in)) : null;
                final String key = keyGenerator.generate(action.getRestful().getMethod(), new URL(action.getURL()), hash);
                final Cache cache = cacheManager.find(key);
                // 没有缓存
                if (cache == null) {
                    action.setRequest(srcRequest);
                    getFromServer(key, callback);
                }
                // 如果缓存是新鲜的
                else if (cache.fresh()) {
                    promise.cancel();
                    getFromCache(cache, callback);
                }
                // 如果缓存是可协商的
                else if (cache.negotiable()) {
                    NegotiatedRequest negotiatedRequest = new NegotiatedRequest(srcRequest);
                    cache.negotiate(negotiatedRequest);
                    action.setRequest(negotiatedRequest);
                    getFromNegotiation(key, cache, callback);
                }
                // 存在缓存但是不是新鲜的也不能协商
                else {
                    action.setRequest(srcRequest);
                    getFromServer(key, callback);
                }
            } catch (Exception e) {
                new Calling(callback, null, e).call();
            }
        }

        private void getFromServer(final String key, final Callback<Object> callback) {
            final Response srcResponse = action.getResponse();
            final NegotiatedResponse negotiatedResponse = new NegotiatedResponse(srcResponse);
            action.setResponse(negotiatedResponse);
            promise.accept(new CallbackAdapter<Object>() {
                @Override
                public void onCompleted(boolean success, Object result, Exception exception) {
                    try {
                        if (success) cacheManager.save(key, negotiatedResponse);
                    } catch (Exception e) {
                        exception = e;
                    } finally {
                        new Calling(callback, result, exception).call();
                    }
                }
            });
        }

        private void getFromCache(final Cache cache, final Callback<Object> callback) {
            client().execute(new Runnable() {
                @Override
                public void run() {
                    Object result = null;
                    Exception exception = null;
                    try {
                        final Response srcResponse = action.getResponse();
                        final CachedResponse cachedResponse = new CachedResponse(srcResponse, cache);
                        action.setResponse(cachedResponse);
                        client().deserialize(action);
                        result = action.getResult().getBody().getValue();
                    } catch (Exception e) {
                        exception = e;
                    } finally {
                        new Calling(callback, result, exception).call();
                    }
                }
            });
        }

        private void getFromNegotiation(final String key, final Cache cache, final Callback<Object> callback) {
            final Response srcResponse = action.getResponse();
            final NegotiatedResponse negotiatedResponse = new NegotiatedResponse(srcResponse);
            action.setResponse(negotiatedResponse);
            promise.accept(new CallbackAdapter<Object>() {
                @Override
                public void onCompleted(boolean success, Object result, Exception exception) {
                    if (cache.negotiated(negotiatedResponse)) {
                        try {
                            result = getFromCache(cache);
                            exception = null;
                        } catch (Exception e) {
                            exception = e;
                        } finally {
                            new Calling(callback, result, exception).call();
                        }
                        return;
                    }
                    try {
                        if (success) cacheManager.save(key, negotiatedResponse);
                    } catch (Exception e) {
                        exception = e;
                    } finally {
                        new Calling(callback, result, exception).call();
                    }
                }
            });
        }

        @Override
        public void cancel() {
            promise.cancel();
        }

        @Override
        public Client client() {
            return promise.client();
        }
    }

}
