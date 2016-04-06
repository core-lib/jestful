package org.qfox.jestful.server;

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
 * @date 2016年4月2日 下午4:52:34
 *
 * @since 1.0.0
 */
@Jestful("/fashion")
public class FashionController {

	@GET("/")
	public void index() {

	}

}
