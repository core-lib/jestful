package org.qfox.elasticsearch.node;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.qfox.jestful.client.Client;

import java.net.URL;

/**
 * 节点API测试类
 *
 * @author Payne 646742615@qq.com
 * 2019/8/16 17:46
 */
public class NodeAPITests {

    @Test
    public void testStatus() throws Exception {
        NodeAPI nodeAPI = Client.builder()
                .setEndpoint(new URL("http://localhost:9200"))
                .build()
                .create(NodeAPI.class);
        NodeStatusResult result = nodeAPI.status();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(System.out, result);
    }

    @Test
    public void testCount() throws Exception {
        NodeAPI nodeAPI = Client.builder()
                .setEndpoint(new URL("http://localhost:9200"))
                .build()
                .create(NodeAPI.class);
        NodeCountResult result = nodeAPI.count();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(System.out, result);
    }

}
