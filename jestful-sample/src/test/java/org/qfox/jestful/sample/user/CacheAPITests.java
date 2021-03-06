package org.qfox.jestful.sample.user;

import org.junit.Test;
import org.qfox.jestful.client.cache.CacheController;
import org.qfox.jestful.client.cache.impl.FileDataStorage;
import org.qfox.jestful.client.cache.impl.http.HttpCacheManager;
import org.qfox.jestful.client.nio.NioClient;
import org.qfox.jestful.client.scheduler.CallbackAdapter;

import java.util.concurrent.CountDownLatch;

public class CacheAPITests {

    @Test
    public void get() throws Exception {
        CacheAPI cacheAPI = NioClient.builder()
                .setProtocol("http")
                .setHostname("localhost")
                .setPort(8080)
                .setKeepAlive(true)
                .setIdleTimeout(10)
                .build()
                .creator()
                .addBackPlugins(CacheController.builder().setCacheManager(new HttpCacheManager(new FileDataStorage())).build())
                .create(CacheAPI.class);

        CountDownLatch latch = new CountDownLatch(2);
        cacheAPI.get(true, new CallbackAdapter<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println(result);
            }

            @Override
            public void onFail(Exception exception) {
                exception.printStackTrace();
            }

            @Override
            public void onCompleted(boolean success, String result, Exception exception) {
                latch.countDown();
                cacheAPI.get(true, new CallbackAdapter<String>() {
                    @Override
                    public void onSuccess(String result) {
                        System.out.println(result);
                    }

                    @Override
                    public void onFail(Exception exception) {
                        exception.printStackTrace();
                    }

                    @Override
                    public void onCompleted(boolean success, String result, Exception exception) {
                        latch.countDown();

                    }
                });
            }
        });
        latch.await();
    }

    @Test
    public void post() throws Exception {
        CacheAPI cacheAPI = NioClient.builder()
                .setProtocol("http")
                .setHostname("localhost")
                .setPort(8080)
                .setKeepAlive(true)
                .setIdleTimeout(10)
                .build()
                .creator()
                .addBackPlugins(new CacheController())
                .create(CacheAPI.class);

        CountDownLatch latch = new CountDownLatch(2);
        cacheAPI.post("Payne", new CallbackAdapter<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println(result);
            }

            @Override
            public void onFail(Exception exception) {
                exception.printStackTrace();
            }

            @Override
            public void onCompleted(boolean success, String result, Exception exception) {
                latch.countDown();
                cacheAPI.post("Payne", new CallbackAdapter<String>() {
                    @Override
                    public void onSuccess(String result) {
                        System.out.println(result);
                    }

                    @Override
                    public void onFail(Exception exception) {
                        exception.printStackTrace();
                    }

                    @Override
                    public void onCompleted(boolean success, String result, Exception exception) {
                        latch.countDown();

                    }
                });
            }
        });
        latch.await();
    }

}
