package org.qfox.elasticsearch.node;

import org.junit.Test;
import org.qfox.elasticsearch.BasicAPITest;
import rx.functions.Action1;

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
        nodeAPI.status().subscribe(new Action1<NodeStatusResult>() {
            @Override
            public void call(NodeStatusResult nodeStatusResult) {
                print(nodeStatusResult);
            }
        });
    }

    @Test
    public void testCount() {
        nodeAPI.count().subscribe(new Action1<NodeCountResult>() {
            @Override
            public void call(NodeCountResult nodeCountResult) {
                print(nodeCountResult);
            }
        });
    }

}
