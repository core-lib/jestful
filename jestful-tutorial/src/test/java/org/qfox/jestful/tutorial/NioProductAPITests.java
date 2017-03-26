package org.qfox.jestful.tutorial;

import org.junit.Test;
import org.qfox.jestful.client.nio.NioClient;
import org.qfox.jestful.client.scheduler.Callback;

import java.util.List;

/**
 * Created by payne on 2017/3/26.
 */
public class NioProductAPITests {

    @Test
    public void testJson() throws Exception {
        NioProductAPI api = NioClient.builder().build().create(NioProductAPI.class, "http://localhost:8080");
        api.json(123L, new Callback<List<Long>>() {
            @Override
            public void onCompleted(boolean success, List<Long> result, Throwable throwable) {

            }

            @Override
            public void onSuccess(List<Long> result) {

            }

            @Override
            public void onFail(Throwable throwable) {

            }
        });
        Thread.sleep(10 * 1000L);
    }

}
