package org.qfox.jestful.tutorial;

import org.junit.Test;
import org.qfox.jestful.client.Client;
import org.qfox.jestful.client.aio.AioClient;
import org.qfox.jestful.client.nio.NioClient;
import org.qfox.jestful.client.scheduler.Callback;

/**
 * Created by payne on 2017/3/27.
 */
public class ProxyAPITests {

    @Test
    public void testBioProxy() throws Exception {
        ProxyAPI api = Client.builder().build().create(ProxyAPI.class, "http://www.httpwatch.com");
        String image = api.index();
        System.out.println(image);
    }

    @Test
    public void testNioProxy() throws Exception {
        ProxyAPI api = NioClient.builder().build().create(ProxyAPI.class, "http://www.httpwatch.com");
        api.index(new Callback<String>() {
            @Override
            public void onCompleted(boolean success, String result, Throwable throwable) {
                synchronized (ProxyAPITests.this) {
                    ProxyAPITests.this.notifyAll();
                }
            }

            @Override
            public void onSuccess(String result) {

            }

            @Override
            public void onFail(Throwable throwable) {

            }
        });
        synchronized (this) {
            this.wait();
        }
    }

    @Test
    public void testAioProxy() throws Exception {
        ProxyAPI api = AioClient.builder().build().create(ProxyAPI.class, "http://www.httpwatch.com");
        api.index(new Callback<String>() {
            @Override
            public void onCompleted(boolean success, String result, Throwable throwable) {
                synchronized (ProxyAPITests.this) {
                    ProxyAPITests.this.notifyAll();
                }
            }

            @Override
            public void onSuccess(String result) {

            }

            @Override
            public void onFail(Throwable throwable) {

            }
        });
        synchronized (this) {
            this.wait();
        }
    }

}
