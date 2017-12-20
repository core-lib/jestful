package org.qfox.jestful.sample.user;

import org.junit.Test;
import org.qfox.jestful.client.cache.CacheController;
import org.qfox.jestful.client.cache.CacheManager;
import org.qfox.jestful.client.cache.impl.FileDataStorage;
import org.qfox.jestful.client.cache.impl.http.HttpCacheManager;
import org.qfox.jestful.client.nio.NioClient;
import org.qfox.jestful.client.redirect.Redirector;
import org.qfox.jestful.client.scheduler.CallbackAdapter;

import java.util.concurrent.CountDownLatch;

public class QfoxyAPITest {

    @Test
    public void index() throws Exception {
        CacheManager cacheManager = new HttpCacheManager(new FileDataStorage());
        CacheController cacheController = new CacheController();
        cacheController.setCacheManager(cacheManager);

        QfoxyAPI qfoxyAPI = NioClient.builder()
                .setProtocol("https")
                .setHostname("api.qfoxy.com")
                .setKeepAlive(true)
                .setIdleTimeout(10)
                .build()
                .creator()
                .addBackPlugins(new Redirector())
                .addBackPlugins(cacheController)
                .create(QfoxyAPI.class);

        CountDownLatch latch = new CountDownLatch(100);
        for (int i = 0; i < 100; i++) {
            qfoxyAPI.jQuery(new CallbackAdapter<String>() {
                @Override
                public void onCompleted(boolean success, String result, Exception exception) {
                    if (success) System.out.println(result.length());
                    else exception.printStackTrace();
                    latch.countDown();
                }
            });
        }
        latch.await();
    }

}
