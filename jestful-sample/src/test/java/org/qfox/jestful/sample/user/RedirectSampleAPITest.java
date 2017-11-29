package org.qfox.jestful.sample.user;

import org.junit.Test;
import org.qfox.jestful.client.Client;
import org.qfox.jestful.client.nio.NioClient;
import org.qfox.jestful.client.redirect.Redirector;
import org.qfox.jestful.commons.Lock;
import org.qfox.jestful.commons.SimpleLock;
import org.qfox.jestful.sample.RedirectSampleAPI;

import java.util.StringTokenizer;

/**
 * Created by yangchangpei on 17/9/27.
 */
public class RedirectSampleAPITest {

    @Test
    public void count() {
        System.out.println(getKeepAlive(""));
    }

    public long getKeepAlive(String keepAlive) {
        if (keepAlive == null) return -1; // NOT SET
        try {
            // Keep-Alive: seconds
            return Long.valueOf(keepAlive);
        } catch (Exception e) {
            // Keep-Alive: timeout=seconds
            StringTokenizer tokenizer = new StringTokenizer(keepAlive, ",; ");
            while (tokenizer.hasMoreTokens()) {
                String element = tokenizer.nextToken();
                StringTokenizer t = new StringTokenizer(element, "=: ");
                if (t.countTokens() != 2) continue;
                String name = t.nextToken();
                String value = t.nextToken();
                if (name.equalsIgnoreCase("timeout")) return Long.valueOf(value);
            }
            return -1;
        }
    }

    @Test
    public void get() throws Exception {
        NioClient client = NioClient.builder()
                .setProtocol("http")
                .setHostname("localhost")
                .setKeepAlive(true)
                .build();
        RedirectSampleAPI userAPI = client
                .creator()
                .addBackPlugins(new Redirector())
                .create(RedirectSampleAPI.class);

        for (int i = 0; i < 100; i++) {
            User user = userAPI.source();
            System.out.println(user);
        }
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