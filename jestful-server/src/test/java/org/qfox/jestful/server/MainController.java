package org.qfox.jestful.server;

import java.util.List;

import org.qfox.jestful.core.annotation.GET;
import org.qfox.jestful.core.annotation.Jestful;
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
 * @date 2016年4月6日 下午12:10:55
 *
 * @since 1.0.0
 */
@Jestful("/")
public class MainController implements ResourceController<Object> {

	@GET("/page-{page}/size-{size}")
	public List<Object> index(@Path("page") int page, @Path("size") int size) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object find(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	public Long create(Object domain) {
		// TODO Auto-generated method stub
		return null;
	}

	public void update(Object domain) {
		// TODO Auto-generated method stub

	}

	public void remove(Long id) {
		// TODO Auto-generated method stub

	}

}
