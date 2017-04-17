package org.qfox.jestful.tutorial;

import org.junit.Test;
import org.qfox.jestful.client.Client;
import org.qfox.jestful.client.scheduler.Callback;
import org.qfox.jestful.commons.Lock;
import org.qfox.jestful.commons.SimpleLock;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by payne on 2017/4/4.
 * Version: 1.0
 */
public class NioSSLTests {
    public NioSSLTests() throws MalformedURLException {
    }

    @Test
    public void test() throws Exception {
        ProxyAPI proxyAPI = Client.builder().addPlugins("characterEncodingPlugin; charset=UTF-8").setConnTimeout(1000 * 1000).setEndpoint(new URL("http://merchant.qfoxtech.com")).build().create(ProxyAPI.class);

        Lock lock = new SimpleLock();
        proxyAPI.index(new Callback<String>() {
            @Override
            public void onCompleted(boolean success, String result, Throwable throwable) {
                lock.openAll();
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
