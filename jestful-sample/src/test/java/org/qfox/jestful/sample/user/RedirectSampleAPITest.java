package org.qfox.jestful.sample.user;

import org.junit.Test;
import org.qfox.jestful.client.aio.AioClient;
import org.qfox.jestful.client.redirect.Redirector;
import org.qfox.jestful.sample.RedirectSampleAPI;

import java.util.concurrent.CountDownLatch;

/**
 * Created by yangchangpei on 17/9/27.
 */
public class RedirectSampleAPITest {

    @Test
    public void count() {
    }

    @Test
    public void get() throws Exception {
        RedirectSampleAPI redirectSampleAPI = AioClient.builder()
                .setProtocol("http")
                .setHostname("localhost")
                .setPort(8080)
                .setKeepAlive(true)
                .setIdleTimeout(100)
                .build()
                .creator()
                .addBackPlugins(new Redirector())
                .create(RedirectSampleAPI.class);

        CountDownLatch latch = new CountDownLatch(100);
        for (int i = 0; i < 100; i++) {
            redirectSampleAPI.source((success, user, exception) -> {
                System.out.println(user);
                latch.countDown();
            });
        }
        latch.await();
    }

    @Test
    public void post() throws Exception {
        AioClient client = AioClient.builder()
                .setProtocol("http")
                .setHostname("localhost")
                .setPort(8080)
                .setKeepAlive(true)
                .setIdleTimeout(100)
                .build();
        RedirectSampleAPI userAPI = client.creator()
                .addBackPlugins(new Redirector())
                .create(RedirectSampleAPI.class);

        User body = new User("Change", 2L, null, null);

        CountDownLatch latch = new CountDownLatch(100);
        for (int i = 0; i < 100; i++)
            userAPI.source("Programmer", body, (success, result, exception) -> {
                System.out.println(result);
                latch.countDown();
            });
        latch.await();
        client.destroy();
    }

}