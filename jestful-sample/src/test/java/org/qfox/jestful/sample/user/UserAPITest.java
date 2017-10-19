package org.qfox.jestful.sample.user;

import org.junit.Test;
import org.qfox.jestful.client.scheduler.Callback;
import org.qfox.jestful.commons.Base64;
import org.qfox.jestful.commons.Lock;
import org.qfox.jestful.commons.SimpleLock;

import java.util.Map;
import java.util.concurrent.Future;

/**
 * Created by yangchangpei on 17/9/27.
 */
public class UserAPITest {

    @Test
    public void getUserSynchronously() throws Exception {
//        Map<String, Object> user = UserAPI.NIO.user("Basic " + Base64.encode("core-lib:wan20100101"));
//        System.out.println(user);

        Future<Map<String, Object>> future = UserAPI.NIO.userOfFuture("Basic " + Base64.encode("core-lib:wan20100101"));
        Map<String, Object> map = future.get();
        System.out.println(map);
    }

    @Test
    public void getUserAsynchronously() throws Exception {
        final Lock lock = new SimpleLock();
        UserAPI.NIO.user("Basic " + Base64.encode("core-lib:wan20100101"), new Callback<Map<String, Object>>() {
            @Override
            public void onCompleted(boolean success, Map<String, Object> result, Exception exception) {
                lock.openAll();
            }

            @Override
            public void onSuccess(Map<String, Object> result) {
                System.out.println(result);
            }

            @Override
            public void onFail(Exception exception) {
                exception.printStackTrace();
            }
        });
        lock.lockOne();
    }

}