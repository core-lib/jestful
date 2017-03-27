package org.qfox.jestful.tutorial;

import org.junit.Test;
import org.qfox.jestful.client.Client;
import org.qfox.jestful.client.nio.NioClient;
import org.qfox.jestful.client.scheduler.Callback;

/**
 * Created by payne on 2017/3/26.
 */
public class CookieAPITests {

    @Test
    public void testBioCookie() throws Exception {
        final Object lock = new Object();
        final CookieAPI api = Client.builder().addPlugins("cookie").build().create(CookieAPI.class, "http://localhost:8080");
        api.index(new Callback<String>() {
            @Override
            public void onCompleted(boolean success, String result, Throwable throwable) {
                api.index(new Callback<String>() {
                    @Override
                    public void onCompleted(boolean success, String result, Throwable throwable) {
                        synchronized (lock) {
                            lock.notifyAll();
                        }
                    }

                    @Override
                    public void onSuccess(String result) {

                    }

                    @Override
                    public void onFail(Throwable throwable) {

                    }
                });
            }

            @Override
            public void onSuccess(String result) {

            }

            @Override
            public void onFail(Throwable throwable) {

            }
        });
        synchronized (lock) {
            lock.wait();
        }
    }

    @Test
    public void testNioCookie() throws Exception {
        final Object lock = new Object();
        final CookieAPI api = NioClient.builder().setSelectTimeout(1000).setAcceptEncode(true).addPlugins("cookie").build().create(CookieAPI.class, "http://localhost:8080");
        api.index(new Callback<String>() {
            @Override
            public void onCompleted(boolean success, String result, Throwable throwable) {
                api.index(new Callback<String>() {
                    @Override
                    public void onCompleted(boolean success, String result, Throwable throwable) {
                        synchronized (lock) {
                            lock.notifyAll();
                        }
                    }

                    @Override
                    public void onSuccess(String result) {

                    }

                    @Override
                    public void onFail(Throwable throwable) {

                    }
                });
            }

            @Override
            public void onSuccess(String result) {

            }

            @Override
            public void onFail(Throwable throwable) {

            }
        });
        synchronized (lock) {
            lock.wait();
        }
    }

    @Test
    public void testNioTimeout() throws Exception {
        final Object lock = new Object();
        final CookieAPI api = NioClient.builder().setSelectTimeout(1000).setReadTimeout(5 * 1000).setAcceptEncode(true).addPlugins("cookie").build().create(CookieAPI.class, "http://localhost:8080");
        api.index(new Callback<String>() {
            @Override
            public void onCompleted(boolean success, String result, Throwable throwable) {
                synchronized (lock) {
                    lock.notifyAll();
                }
            }

            @Override
            public void onSuccess(String result) {

            }

            @Override
            public void onFail(Throwable throwable) {

            }
        });
        synchronized (lock) {
            lock.wait();
        }
    }

}
