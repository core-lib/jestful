package org.qfox.jestful.tutorial;

import org.junit.Test;
import org.qfox.jestful.client.Client;

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
        PathControllerAPI api = Client.getDefaultClient().create(PathControllerAPI.class, "http://localhost/");
        String result = api.get("中文", 12L);
        System.out.println(result);
        Client.getDefaultClient().destroy();
    }

}
