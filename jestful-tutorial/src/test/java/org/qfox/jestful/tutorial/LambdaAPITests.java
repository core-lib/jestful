package org.qfox.jestful.tutorial;

import org.junit.Test;
import org.qfox.jestful.client.Client;
import org.qfox.jestful.commons.Lock;
import org.qfox.jestful.commons.SimpleLock;

/**
 * Created by yangchangpei on 17/3/31.
 */
public class LambdaAPITests {

    @Test
    public void testIndex() throws Exception {
        Lock lock = new SimpleLock();
        LambdaAPI<String> lambdaAPI = Client.builder().setHost("localhost").setPort(8080).build().create(LambdaAPI.class);
        lambdaAPI.index("add",
                System.out::println,
                Throwable::printStackTrace,
                (success, result, throwable) -> lock.openAll());
        lock.lockOne();
    }

    @Test
    public void testCall() throws Exception {
        Lock lock = new SimpleLock();
        LambdaAPI<String> lambdaAPI = Client.builder().setHost("localhost").setPort(8080).build().create(LambdaAPI.class);
        lambdaAPI.call("add",
                System.out::println,
                Throwable::printStackTrace,
                (success, result, throwable) -> lock.openAll());
        lock.lockOne();
    }

}
