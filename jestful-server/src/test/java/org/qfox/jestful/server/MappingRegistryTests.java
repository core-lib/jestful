package org.qfox.jestful.server;

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
 * @date 2016年3月31日 下午12:19:34
 *
 * @since 1.0.0
 */
public class MappingRegistryTests {

	@Test
	public void test() throws Exception {
		MappingRegistry registry = new TreeMappingRegistry("/");
		registry.register(new BrandController());
		System.out.println(registry);
	}

}
