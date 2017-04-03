package org.qfox.jestful.tutorial;

import org.junit.Test;
import org.qfox.jestful.client.Client;
import org.qfox.jestful.client.aio.AioClient;
import org.qfox.jestful.client.nio.NioClient;
import org.qfox.jestful.commons.Lock;
import org.qfox.jestful.commons.SimpleLock;

import java.net.URL;

/**
 * Created by yangchangpei on 17/3/31.
 */
public class LambdaAPITests {

    @Test
    public void testBioParameterTypeVariable() throws Exception {
        Lock lock = new SimpleLock();
        LambdaAPI lambdaAPI = Client.builder().setHost("localhost").setPort(8080).build().create(LambdaAPI.class);
        lambdaAPI.callWithParameterTypeVariable("add",
                System.out::println,
                Throwable::printStackTrace,
                (success, result, throwable) -> lock.openAll());
        lock.lockOne();
    }

    @Test
    public void testBioMethodTypeVariable() throws Exception {
        Lock lock = new SimpleLock();
        LambdaAPI lambdaAPI = Client.builder().setHost("localhost").setPort(8080).build().create(LambdaAPI.class);
        lambdaAPI.callWithMethodTypeVariable("add",
                System.out::println,
                Throwable::printStackTrace,
                (success, result, throwable) -> lock.openAll());
        lock.lockOne();
    }

    @Test
    public void testBioInterfaceTypeVariable() throws Exception {
        Lock lock = new SimpleLock();
        LambdaAPI lambdaAPI = Client.builder().setHost("localhost").setPort(8080).build().create(LambdaAPI.class);
        lambdaAPI.callWithInterfaceTypeVariable("add",
                System.out::println,
                Throwable::printStackTrace,
                (success, result, throwable) -> lock.openAll());
        lock.lockOne();
    }


    @Test
    public void testNioParameterTypeVariable() throws Exception {
        Lock lock = new SimpleLock();
        LambdaAPI lambdaAPI = NioClient.builder().setHost("localhost").setPort(8080).build().create(LambdaAPI.class);
        lambdaAPI.callWithParameterTypeVariable("add",
                System.out::println,
                Throwable::printStackTrace,
                (success, result, throwable) -> lock.openAll());
        lock.lockOne();
    }

    @Test
    public void testNioMethodTypeVariable() throws Exception {
        Lock lock = new SimpleLock();
        LambdaAPI lambdaAPI = NioClient.builder().setHost("localhost").setPort(8080).build().create(LambdaAPI.class);
        lambdaAPI.callWithMethodTypeVariable("add",
                System.out::println,
                Throwable::printStackTrace,
                (success, result, throwable) -> lock.openAll());
        lock.lockOne();
    }

    @Test
    public void testNioInterfaceTypeVariable() throws Exception {
        Lock lock = new SimpleLock();
        LambdaAPI lambdaAPI = NioClient.builder().setHost("localhost").setPort(8080).build().create(LambdaAPI.class);
        lambdaAPI.callWithInterfaceTypeVariable("add",
                System.out::println,
                Throwable::printStackTrace,
                (success, result, throwable) -> lock.openAll());
        lock.lockOne();
    }

    @Test
    public void testAioParameterTypeVariable() throws Exception {
        Lock lock = new SimpleLock();
        LambdaAPI lambdaAPI = AioClient.builder().setHost("localhost").setPort(8080).build().create(LambdaAPI.class);
        lambdaAPI.callWithParameterTypeVariable("add",
                System.out::println,
                Throwable::printStackTrace,
                (success, result, throwable) -> lock.openAll());
        lock.lockOne();
    }

    @Test
    public void testAioMethodTypeVariable() throws Exception {
        Lock lock = new SimpleLock();
        LambdaAPI lambdaAPI = AioClient.builder().setHost("localhost").setPort(8080).build().create(LambdaAPI.class);
        lambdaAPI.callWithMethodTypeVariable("add",
                System.out::println,
                Throwable::printStackTrace,
                (success, result, throwable) -> lock.openAll());
        lock.lockOne();
    }

    @Test
    public void testAioInterfaceTypeVariable() throws Exception {
        Lock lock = new SimpleLock();
        LambdaAPI lambdaAPI = AioClient.builder().setHost("localhost").setPort(8080).build().create(LambdaAPI.class);
        lambdaAPI.callWithInterfaceTypeVariable("add",
                System.out::println,
                Throwable::printStackTrace,
                (success, result, throwable) -> lock.openAll());
        lock.lockOne();
    }

}
