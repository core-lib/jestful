package org.qfox.jestful.sample.user;

import org.junit.Test;
import org.qfox.jestful.client.aio.AioClient;
import org.qfox.jestful.client.redirect.Redirector;
import org.qfox.jestful.commons.Lock;
import org.qfox.jestful.commons.SimpleLock;
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
        RedirectSampleAPI userAPI = AioClient.builder()
                .setProtocol("http")
                .setHostname("localhost")
                .setPort(8080)
                .setKeepAlive(true)
                .setIdleTimeout(100)
                .build()
                .creator()
                .addBackPlugins(new Redirector())
                .create(RedirectSampleAPI.class);

        User body = new User("Change", 2L, null, null);
        User user = userAPI.source("Programmer", body);
        System.out.println(user);

        Lock lock = new SimpleLock();
        userAPI.source("Programmer", user, (success, result, exception) -> {
            System.out.println(result);
            lock.openAll();
        });
        lock.lockOne();


    }

}