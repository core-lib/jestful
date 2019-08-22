package org.qfox.elasticsearch.node;

import org.junit.Test;
import org.qfox.elasticsearch.BasicAPITest;

/**
 * 节点API测试类
 *
 * @author Payne 646742615@qq.com
 * 2019/8/16 17:46
 */
public class NodeAPITests extends BasicAPITest {

    private NodeAPI nodeAPI;

    @Test
    public void testStatus() throws Exception {
        NodeStatusResult result = nodeAPI.status();
        print(result);
    }

    @Test
    public void testCount() throws Exception {
        NodeCountResult result = nodeAPI.count();
        print(result);
    }

}
