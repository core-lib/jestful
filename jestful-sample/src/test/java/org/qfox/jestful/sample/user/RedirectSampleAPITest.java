package org.qfox.jestful.sample.user;

import org.junit.Test;
import org.qfox.jestful.client.Client;
import org.qfox.jestful.client.nio.NioClient;
import org.qfox.jestful.client.redirect.Redirector;
import org.qfox.jestful.commons.Lock;
import org.qfox.jestful.commons.SimpleLock;
import org.qfox.jestful.sample.RedirectSampleAPI;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by yangchangpei on 17/9/27.
 */
public class RedirectSampleAPITest {

    @Test
    public void get() throws Exception {
        RedirectSampleAPI userAPI = NioClient.builder()
                .setProtocol("http")
                .setHostname("localhost")
                .setPort(8080)
                .setKeepAlive(true)
                .build()
                .creator()
                .addBackPlugins(new Redirector())
                .create(RedirectSampleAPI.class);

        Lock lock = new SimpleLock();
        AtomicInteger remain = new AtomicInteger(100);
        for (int i = 0; i < 100; i++) {
            new Thread(() -> {
                User user = userAPI.source();
//                System.out.println(user);
                if (remain.decrementAndGet() == 0) {
                    lock.openAll();
                }
            }).start();
        }
        lock.lockOne();
    }

    @Test
    public void post() throws Exception {
        RedirectSampleAPI userAPI = Client.builder()
                .setProtocol("http")
                .setHostname("localhost")
                .setPort(8080)
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