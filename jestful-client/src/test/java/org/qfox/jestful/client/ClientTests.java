package org.qfox.jestful.client;

import java.net.URL;
import java.util.List;

import org.junit.Test;

/**
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 * 
 * @author Payne 646742615@qq.com
 *
 * @date 2016年4月27日 下午7:07:43
 *
 * @since 1.0.0
 */
public class ClientTests {

	@Test
	public void testCreate() throws Exception {
		List<Product> products = Client.builder().setEndpoint(new URL("http://localhost:8080")).build().create(ProductController.class).list(1, 10);
		System.out.println(products);
	}

}
