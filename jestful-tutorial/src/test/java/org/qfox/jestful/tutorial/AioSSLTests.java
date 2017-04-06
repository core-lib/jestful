package org.qfox.jestful.tutorial;

import org.junit.Test;
import org.qfox.jestful.client.aio.AioClient;
import org.qfox.jestful.client.nio.NioClient;
import org.qfox.jestful.client.scheduler.Callback;
import org.qfox.jestful.commons.Lock;
import org.qfox.jestful.commons.SimpleLock;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by payne on 2017/4/4.
 * Version: 1.0
 */
public class AioSSLTests {
    ProxyAPI proxyAPI = AioClient.builder().setConnTimeout(1000 * 1000).setEndpoint(new URL("https://merchant.qfoxy.com/v4/login")).build().create(ProxyAPI.class);

    public AioSSLTests() throws MalformedURLException {
    }

    @Test
    public void test() throws Exception {
        Lock lock = new SimpleLock();

        proxyAPI.index(new Callback<String>() {
            @Override
            public void onCompleted(boolean success, String result, Throwable throwable) {
                try {
                    test();
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
