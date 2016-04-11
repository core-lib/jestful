package org.qfox.jestful.server;

import org.junit.Test;
import org.qfox.jestful.core.Mapping;

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
	private MappingRegistry registry = null;

	@Test
	public void testRegister() throws Exception {
		registry = new TreeMappingRegistry();
		registry.register(new MainController());
		registry.register(new BrandController());
		registry.register(new ProductController());
		System.out.println(registry);
	}

	@Test
	public void testLookup() throws Exception {
		testRegister();
		Mapping mapping = registry.lookup("GET", "/page-12/size-10");
		System.out.println(mapping);
	}

}
