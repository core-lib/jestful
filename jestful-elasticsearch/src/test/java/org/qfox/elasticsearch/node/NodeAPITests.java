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
    public void testStatus() {
        nodeAPI.status();
    }

    @Test
    public void testCount() {
        nodeAPI.count();
    }

}
