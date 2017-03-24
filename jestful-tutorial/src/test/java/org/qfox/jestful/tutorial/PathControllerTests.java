package org.qfox.jestful.tutorial;

import org.junit.Test;
import org.qfox.jestful.client.Client;
import org.qfox.jestful.client.nio.NioClient;
import sun.security.action.GetPropertyAction;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.AccessController;
import java.util.Map;
import java.util.Properties;

/**
 * Created by payne on 2017/3/5.
 */
public class PathControllerTests {

    @Test
    public void testURL() throws MalformedURLException {
        new URL("http://localhost:-1");
        Properties env = System.getProperties();
        for (Map.Entry<Object, Object> entry : env.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
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
