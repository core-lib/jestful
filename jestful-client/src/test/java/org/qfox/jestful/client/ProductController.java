package org.qfox.jestful.client;

import org.qfox.jestful.client.annotation.Queries;
import org.qfox.jestful.core.annotation.Body;
import org.qfox.jestful.core.annotation.Jestful;
import org.qfox.jestful.core.annotation.POST;
import org.qfox.jestful.core.annotation.Path;
import org.qfox.jestful.core.annotation.Query;

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
 * @date 2016年4月27日 下午7:07:05
 *
 * @since 1.0.0
 */
@Jestful("/product")
public interface ProductController {

	@Queries("q3=中文")
	@POST(value = "/page={page:\\d+}/size={size:\\d+}.res")
	public String index(@Path("page") int page, @Path("size") int size, @Query("q1") String q1, @Query("q2") long q2, @Body("body") String body);

}
