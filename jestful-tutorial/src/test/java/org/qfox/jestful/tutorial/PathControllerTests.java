package org.qfox.jestful.tutorial;

import org.junit.Test;
import org.qfox.jestful.client.Client;
import org.qfox.jestful.client.nio.NioClient;

import java.net.MalformedURLException;
import java.net.URL;
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
        PathControllerAPI api = NioClient.builder().setAcceptEncode(false).build().create(PathControllerAPI.class, "http://localhost:8080");
        String result = api.get("sdfsdf", 12L);
        System.out.println(result);
        Client.getDefaultClient().destroy();
    }

}
