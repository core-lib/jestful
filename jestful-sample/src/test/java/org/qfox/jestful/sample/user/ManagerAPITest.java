package org.qfox.jestful.sample.user;

import org.junit.Test;
import org.qfox.jestful.client.auth.Authenticator;
import org.qfox.jestful.client.auth.CredenceProvider;
import org.qfox.jestful.client.auth.Scope;
import org.qfox.jestful.client.auth.impl.SimpleCredence;
import org.qfox.jestful.client.nio.NioClient;
import org.qfox.jestful.client.scheduler.Callback;
import org.qfox.jestful.commons.Lock;
import org.qfox.jestful.commons.SimpleLock;
import org.qfox.jestful.sample.ManagerAPI;

/**
 * Created by yangchangpei on 17/9/27.
 */
public class ManagerAPITest {

    @Test
    public void getUserSynchronously() throws Exception {
        Authenticator authenticator = new Authenticator();
        CredenceProvider credenceProvider = authenticator.getCredenceProvider();
        credenceProvider.setCredence(new Scope("192.168.31.200", 8080), new SimpleCredence("s", "tomcat"));
        ManagerAPI managerAPI = NioClient.builder()
                .setProtocol("http")
                .setHostname("192.168.31.200")
                .setPort(8080)
                .build()
                .creator()
                .addBackPlugins(authenticator)
                .create(ManagerAPI.class);

        {
            String html = managerAPI.index("中文");
            System.out.println(html);
        }

        {
            final Lock lock = new SimpleLock();
            managerAPI.index(new Callback<String>() {

                @Override
                public void onCompleted(boolean success, String result, Exception exception) {
                    lock.openAll();
                }

                @Override
                public void onSuccess(String result) {
                    System.out.println(result);
                }

                @Override
                public void onFail(Exception exception) {

                }
            });
            lock.lockOne();
        }
    }

}