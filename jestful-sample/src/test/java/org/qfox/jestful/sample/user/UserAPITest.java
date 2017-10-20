package org.qfox.jestful.sample.user;

import org.junit.Test;
import org.qfox.jestful.client.scheduler.Callback;
import org.qfox.jestful.commons.Base64;
import org.qfox.jestful.commons.Lock;
import org.qfox.jestful.commons.SimpleLock;

import java.util.concurrent.Future;

/**
 * Created by yangchangpei on 17/9/27.
 */
public class UserAPITest {

    @Test
    public void getUserSynchronously() throws Exception {
        User user = UserAPI.AIO.user("Basic " + Base64.encode("core-lib:wan20100101"));
        System.out.println(user);

        Future<User> future = UserAPI.AIO.userOfFuture("Basic " + Base64.encode("core-lib:wan20100101"));
        User map = future.get();
        System.out.println(map);

        UserAPI.AIO.userOfObservable("Basic " + Base64.encode("core-lib:wan20100101"))
                .subscribe(System.out::println);
    }

    @Test
    public void getUserAsynchronously() throws Exception {
        final Lock lock = new SimpleLock();
        UserAPI.BIO.user(null, new Callback<User>() {
            @Override
            public void onCompleted(boolean success, User result, Exception exception) {
                lock.openAll();
            }

            @Override
            public void onSuccess(User result) {
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