package org.qfox.jestful.tutorial;

import org.junit.Test;
import org.qfox.jestful.client.Client;
import org.qfox.jestful.client.nio.NioClient;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by payne on 2017/3/5.
 */
public class PathControllerTests {

    @Test
    public void testURL() throws MalformedURLException {
        new URL("http://localhost:-1");
    }

    @Test
    public void testGet() throws Exception {
        BaiduAPI api = NioClient.builder().setAcceptEncode(false).build().create(BaiduAPI.class, "http://www.qfoxtech.com");
        String result = api.index();
        Thread.sleep(1000 * 10L);
        System.out.println(result);
        Client.getDefaultClient().destroy();
    }

}
