package org.qfox.jestful.sample.user;

import org.junit.Test;
import org.qfox.jestful.client.scheduler.Callback;
import org.qfox.jestful.commons.Base64;
import org.qfox.jestful.commons.Lock;
import org.qfox.jestful.commons.SimpleLock;

import java.util.Map;

/**
 * Created by yangchangpei on 17/9/27.
 */
public class UserAPITest {

    @Test
    public void getUserSynchronously() throws Exception {
        final Lock lock = new SimpleLock();
        UserAPI.BIO.user("Basic " + Base64.encode("core-lib:wan20100101"), new Callback<Map<String, Object>>() {
            @Override
            public void onCompleted(boolean success, Map<String, Object> result, Throwable throwable) {
                lock.openAll();
            }

            @Override
            public void onSuccess(Map<String, Object> result) {
                System.out.println(result);
            }

            @Override
            public void onFail(Throwable throwable) {
                throwable.printStackTrace();
            }
        });
        lock.lockOne();
    }

    @Test
    public void getUserAsynchronously() throws Exception {
        final Lock lock = new SimpleLock();
        UserAPI.NIO.user("Basic " + Base64.encode("core-lib:wan20100101"), new Callback<Map<String, Object>>() {
            @Override
            public void onCompleted(boolean success, Map<String, Object> result, Throwable throwable) {
                lock.openAll();
            }

            @Override
            public void onSuccess(Map<String, Object> result) {
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