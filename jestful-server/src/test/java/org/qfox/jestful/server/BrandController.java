package org.qfox.jestful.server;

import java.util.List;

import org.qfox.jestful.core.annotation.GET;
import org.qfox.jestful.core.annotation.Jestful;

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
 * @date 2016年4月1日 下午8:16:45
 *
 * @since 1.0.0
 */
@Jestful("/fashion/brands")
public class BrandController extends DomainController<Brand> {

	@GET("/index")
	public List<Brand> index(int page, int size) {
		// TODO Auto-generated method stub
		return super.index(page, size);
	}

}
