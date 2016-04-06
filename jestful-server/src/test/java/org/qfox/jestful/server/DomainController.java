package org.qfox.jestful.server;

import java.util.List;

import org.qfox.jestful.core.annotation.Body;
import org.qfox.jestful.core.annotation.Field;
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
 * @date 2016年4月2日 下午3:55:57
 *
 * @since 1.0.0
 */
public class DomainController<T> implements ResourceController<T> {

	public List<T> index(@Field("page") int page, @Field("size") int size) {

		return null;
	}

	public T find(@Path("id") Long id) {

		return null;
	}

	public Long create(@Body T domain) {

		return null;
	}

	public void update(@Body T domain) {

	}

	public void remove(@Path("id") Long id) {

	}

}
