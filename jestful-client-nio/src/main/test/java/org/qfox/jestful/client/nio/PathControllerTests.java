package org.qfox.jestful.client.nio;

import org.junit.Test;
import org.qfox.jestful.client.Client;

/**
 * Created by payne on 2017/3/5.
 */
public class PathControllerTests {

    @Test
    public void testGet() throws Exception {
        PathControllerAPI api = NioClient.getDefaultClient().create(PathControllerAPI.class, "http://localhost/");
        String result = api.get("中文", 12L);
        System.out.println(result);
        Client.getDefaultClient().destroy();
    }

}
