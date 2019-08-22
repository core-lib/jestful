package org.qfox.elasticsearch.document;

import org.junit.Test;
import org.qfox.elasticsearch.BasicAPITest;

import java.math.BigDecimal;

/**
 * 文档API测试类
 *
 * @author Payne 646742615@qq.com
 * 2019/8/22 14:32
 */
public class DocumentAPITest extends BasicAPITest {

    private DocumentAPI documentAPI;

    @Test
    public void testIndex() throws Exception {
        DocumentIndexResult result = documentAPI.index("basic", "product", "3", new Product("iPhone XR", new BigDecimal(5099)));
        print(result);
    }

    @Test
    public void testCreate() throws Exception {
        DocumentIndexResult result = documentAPI.create("basic", "product", new Product("iPhone XR", new BigDecimal(5099)));
        print(result);
    }
}
