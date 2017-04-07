package org.qfox.jestful.tutorial;

import org.junit.Test;
import org.qfox.jestful.client.Client;
import org.qfox.jestful.client.scheduler.Callback;
import org.qfox.jestful.commons.Lock;
import org.qfox.jestful.commons.SimpleLock;

/**
 * Created by yangchangpei on 17/4/7.
 */
public class RedirectTests {

    @Test
    public void testBioRedirect() {
        Lock lock = new SimpleLock();
        ProxyAPI proxyAPI = Client.builder().setRedirectFollowed(true).build().create(ProxyAPI.class, "https://merchant.qfoxy.com/index.jsp");
        proxyAPI.index(new Callback<String>() {
            @Override
            public void onCompleted(boolean success, String result, Throwable throwable) {
                testBioRedirect();
            }

            @Override
            public void onSuccess(String result) {
                System.out.println(result);
            }

            @Override
            public void onFail(Throwable throwable) {
                throwable.printStackTrace();
            }
        });
        lock.lockOne();
    }

}
