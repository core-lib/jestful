package org.qfox.jestful.server;

import java.util.List;

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
 * @date 2016年4月2日 下午4:47:16
 *
 * @since 1.0.0
 */
@Jestful("/shops")
public class ShopController implements ResourceController<Shop> {

	public List<Shop> index(int page, int size) {
		// TODO Auto-generated method stub
		return null;
	}

	public Shop find(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	public Long create(Shop domain) {
		// TODO Auto-generated method stub
		return null;
	}

	public void update(Shop domain) {
		// TODO Auto-generated method stub

	}

	public void remove(Long id) {
		// TODO Auto-generated method stub

	}

}
