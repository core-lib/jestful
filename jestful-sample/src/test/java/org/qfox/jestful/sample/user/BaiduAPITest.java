package org.qfox.jestful.sample.user;

import org.junit.Test;
import org.qfox.jestful.client.auth.Authenticator;
import org.qfox.jestful.client.auth.Scope;
import org.qfox.jestful.client.auth.impl.SimpleCredence;
import org.qfox.jestful.client.nio.NioClient;
import org.qfox.jestful.client.redirect.Redirector;

public class BaiduAPITest {

    @Test
    public void index() throws Exception {
        Authenticator authenticator = new Authenticator();
        authenticator.getCredenceProvider().setCredence(new Scope("api.github.com", Scope.ANY_PORT), new SimpleCredence("core-lib", "wan20100101"));

        BaiduAPI baiduAPI = NioClient.builder()
                .setProtocol("https")
                .setHostname("api.qfoxy.com")
                .setKeepAlive(false)
                .build()
                .creator()
                .setBackPlugins(authenticator)
                .addBackPlugins(new Redirector())
                .create(BaiduAPI.class);

        for (int i = 0; i < 10; i++) {
            String index = baiduAPI.index();
            System.out.println(index);
        }
    }

}
