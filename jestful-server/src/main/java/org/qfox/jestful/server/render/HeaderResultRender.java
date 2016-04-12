package org.qfox.jestful.server.render;

import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Actor;
import org.qfox.jestful.core.Location;
import org.qfox.jestful.core.Result;

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
 * @date 2016年4月12日 下午8:23:27
 *
 * @since 1.0.0
 */
public class HeaderResultRender implements Actor {

	public Object react(Action action) throws Exception {
		Result result = action.getResult();
		if (result.getLocation() != Location.HEADER) {
			return action.execute();
		}

		Object value = result.getValue();
		if (value == null) {
			return action.execute();
		}
		
		

		return null;
	}

}
