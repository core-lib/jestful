package org.qfox.jestful.sample.user;

import org.junit.Test;
import org.qfox.jestful.client.Client;
import org.qfox.jestful.client.auth.Authenticator;
import org.qfox.jestful.client.auth.Scope;
import org.qfox.jestful.client.auth.impl.SimpleCredence;
import org.qfox.jestful.client.nio.NioClient;
import org.qfox.jestful.client.redirect.Redirector;
import org.qfox.jestful.commons.Lock;
import org.qfox.jestful.commons.SimpleLock;

/**
 * Created by yangchangpei on 17/9/27.
 */
public class UserAPITest {

    @Test
    public void getUserSynchronously() throws Exception {
        Authenticator authenticator = new Authenticator();
        authenticator.getCredenceProvider().setCredence(new Scope("api.github.com", Scope.ANY_PORT), new SimpleCredence("core-lib", "wan20100101"));

        UserAPI userAPI = Client.builder()
                .setProtocol("https")
                .setHostname("api.github.com")
                .setKeepAlive(false)
                .build()
                .creator()
                .setBackPlugins(authenticator)
                .addBackPlugins(new Redirector())
                .create(UserAPI.class);

        for (int i = 0; i < 10; i++) {
            long start = System.currentTimeMillis();
            User user = userAPI.user();
            long end = System.currentTimeMillis();
            System.out.println(user);
            System.out.println(end - start);
        }
    }

    @Test
    public void getUserAsynchronously() throws Exception {
        Authenticator authenticator = new Authenticator();
        authenticator.getCredenceProvider().setCredence(new Scope("api.github.com", Scope.ANY_PORT), new SimpleCredence("core-lib", "wan20100101"));

        UserAPI userAPI = NioClient.builder()
                .setProtocol("https")
                .setHostname("api.github.com")
                .build()
                .creator()
                .setBackPlugins(authenticator)
                .create(UserAPI.class);

        Lock lock = new SimpleLock();
        userAPI.user(System.out::println, Throwable::printStackTrace, ((success, result, exception) -> lock.openAll()));
        lock.lockOne();
    }

}