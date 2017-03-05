package org.qfox.jestful.tutorial;

import org.junit.Test;
import org.qfox.jestful.client.Client;

/**
 * Created by payne on 2017/3/5.
 */
public class PathControllerTests {

    private final PathControllerAPI api = Client.builder().setHost("localhost").setPort(8080).build().create(PathControllerAPI.class);

    @Test
    public void testGet() throws Exception {
        String result = api.get("中文", 12L);
        System.out.println(result);
    }

}
