package org.qfox.jestful.sample.user;

import org.junit.Test;
import org.qfox.jestful.client.cache.CacheController;
import org.qfox.jestful.client.nio.NioClient;
import org.qfox.jestful.client.scheduler.CallbackAdapter;

import java.util.concurrent.CountDownLatch;

public class QfoxyAPITest {

    @Test
    public void index() throws Exception {
        QfoxyAPI qfoxyAPI = NioClient.builder()
                .setProtocol("https")
                .setHostname("api.qfoxy.com")
                .setKeepAlive(true)
                .setIdleTimeout(10)
                .build()
                .creator()
                .addBackPlugins(new CacheController())
                .create(QfoxyAPI.class);

        CountDownLatch latch = new CountDownLatch(10);
        for (int i = 0; i < 10; i++) {
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
        qfoxyAPI.jQuery(new CallbackAdapter<String>() {
            @Override
            public void onCompleted(boolean success, String result, Exception exception) {
                if (success) System.out.println(result.length());
                else exception.printStackTrace();
            }
        });
        Thread.sleep(1000000);
    }

}
