package org.qfox.jestful.server.render;

import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Actor;
import org.qfox.jestful.core.Movement;
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
 * @date 2016年4月13日 上午11:22:26
 *
 * @since 1.0.0
 */
public class ForwardResultRender implements Actor {

	public Object react(Action action) throws Exception {
		Result result = action.getResult();
		if (result.getMovement() != Movement.FORWARD) {
			return action.execute();
		}
		

		return null;
	}

}
