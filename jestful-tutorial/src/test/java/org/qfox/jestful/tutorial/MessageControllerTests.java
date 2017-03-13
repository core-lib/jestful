package org.qfox.jestful.tutorial;

import org.junit.Test;
import org.qfox.jestful.client.Client;
import org.qfox.jestful.client.Message;

/**
 * Created by yangchangpei on 17/3/13.
 */
public class MessageControllerTests {
    private final MessageControllerAPI api = Client.builder().setHost("localhost").setPort(8080).build().create(MessageControllerAPI.class);

    @Test
    public void testSuccess() {
        Message<String> message = api.success();
        System.out.println(message);
    }

    @Test
    public void testFail() {
        Message<String> message = api.fail();
        System.out.println(message);
    }

    @Test
    public void testRealyFail() {
        api.realyFail();
    }


}
