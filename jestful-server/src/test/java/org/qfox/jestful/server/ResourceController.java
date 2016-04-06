package org.qfox.jestful.server;

import java.util.List;

import org.qfox.jestful.core.annotation.Body;
import org.qfox.jestful.core.annotation.DELETE;
import org.qfox.jestful.core.annotation.Field;
import org.qfox.jestful.core.annotation.GET;
import org.qfox.jestful.core.annotation.POST;
import org.qfox.jestful.core.annotation.PUT;
import org.qfox.jestful.core.annotation.Path;

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
 * @date 2016年4月2日 下午4:18:40
 *
 * @since 1.0.0
 */
public interface ResourceController<T> {

	@GET("/")
	List<T> index(@Field("page") int page, @Field("size") int size);

	@GET("/{id:\\d+}")
	T find(@Path("id") Long id);

	@POST("/")
	Long create(@Body T domain);

	@PUT("/")
	void update(@Body T domain);

	@DELETE("/{id:\\d+}")
	void remove(@Path("id") Long id);

}
