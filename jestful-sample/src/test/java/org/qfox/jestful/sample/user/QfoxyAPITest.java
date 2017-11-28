package org.qfox.jestful.sample.user;

import org.junit.Test;
import org.qfox.jestful.client.aio.AioClient;
import org.qfox.jestful.client.auth.Authenticator;
import org.qfox.jestful.client.auth.Scope;
import org.qfox.jestful.client.auth.impl.SimpleCredence;
import org.qfox.jestful.client.redirect.Redirector;

public class QfoxyAPITest {

    @Test
    public void index() throws Exception {
        Authenticator authenticator = new Authenticator();
        authenticator.getCredenceProvider().setCredence(new Scope("api.github.com", Scope.ANY_PORT), new SimpleCredence("core-lib", "wan20100101"));

        QfoxyAPI qfoxyAPI = AioClient.builder()
                .setProtocol("https")
                .setHostname("api.qfoxy.com")
                .setKeepAlive(true)
                .build()
                .creator()
                .setBackPlugins(authenticator)
                .addBackPlugins(new Redirector())
                .create(QfoxyAPI.class);

        for (int i = 0; i < 10; i++) {
            String index = qfoxyAPI.index();
            System.out.println(index.length());
        }
    }

}
