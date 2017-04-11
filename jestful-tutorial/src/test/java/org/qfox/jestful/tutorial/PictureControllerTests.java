package org.qfox.jestful.tutorial;

import org.junit.Test;
import org.qfox.jestful.client.Client;
import org.qfox.jestful.client.aio.AioClient;
import org.qfox.jestful.client.nio.NioClient;
import org.qfox.jestful.commons.Lock;
import org.qfox.jestful.commons.SimpleLock;

import java.io.File;

/**
 * Created by yangchangpei on 17/4/11.
 */
public class PictureControllerTests {

    @Test
    public void testBioUpload() throws Exception {
        Lock lock = new SimpleLock();
        PictureControllerAPI api = Client.builder().build().create(PictureControllerAPI.class, "http://localhost:8080");
        api.upload(new File("/Users/yangchangpei/Documents/IMG_1227.jpg"), ((success, result, throwable) -> {
            System.out.println(result);
            lock.openAll();
        }));
        lock.lockOne();
    }

    @Test
    public void testNioUpload() throws Exception {
        Lock lock = new SimpleLock();
        PictureControllerAPI api = NioClient.builder().build().create(PictureControllerAPI.class, "http://localhost:8080");
        api.upload(new File("/Users/yangchangpei/Documents/IMG_1227.jpg"), ((success, result, throwable) -> {
            System.out.println(result);
            lock.openAll();
        }));
        lock.lockOne();
    }

    @Test
    public void testAioUpload() throws Exception {
        Lock lock = new SimpleLock();
        PictureControllerAPI api = AioClient.builder().build().create(PictureControllerAPI.class, "http://localhost:8080");
        api.upload(new File("/Users/yangchangpei/Documents/IMG_1227.jpg"), ((success, result, throwable) -> {
            System.out.println(result);
            lock.openAll();
        }));
        lock.lockOne();
    }

}
