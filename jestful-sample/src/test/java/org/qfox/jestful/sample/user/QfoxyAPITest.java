package org.qfox.jestful.sample.user;

import org.junit.Test;
import org.qfox.jestful.client.Client;
import org.qfox.jestful.client.cache.CacheController;

public class QfoxyAPITest {

    @Test
    public void index() throws Exception {
        QfoxyAPI qfoxyAPI = Client.builder()
                .setProtocol("https")
                .setHostname("api.qfoxy.com")
                .setPort(443)
                .setKeepAlive(true)
                .setIdleTimeout(10)
                .build()
                .creator()
                .addBackPlugins(new CacheController())
                .create(QfoxyAPI.class);

        for (int i = 0; i < 10; i++) {
            String index = qfoxyAPI.jQuery();
            System.out.println(index.length());
        }
    }

}
