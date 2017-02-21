package org.qfox.jestful.tutorial;

import org.junit.Test;
import org.qfox.jestful.client.Client;

/**
 * Created by yangchangpei on 17/2/21.
 */
public class EncodeControllerAPITest {
    private final EncodeControllerAPI api = Client.builder().setHost("localhost").setPort(8080).build().create(EncodeControllerAPI.class);

    @Test
    public void testQuery() throws Exception {
        api.testQuery("杨昌沛", "杨昌沛", "杨昌沛");
    }

    @Test
    public void testHeader() throws Exception {
        api.testHeader("杨昌沛", "杨昌沛", "杨昌沛");
        api.testHeader2();
    }

    @Test
    public void testCookie() throws Exception {
        api.testCookie("杨昌沛", "杨昌沛", "杨昌沛");
    }

    @Test
    public void testBody() throws Exception {
        api.testBody("杨昌沛");
    }

}
