package org.qfox.jestful.server.resolver;

import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Actor;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

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
 * @date 2016年4月8日 下午3:42:21
 *
 * @since 1.0.0
 */
public class BodyArgumentResolver implements Actor, ApplicationContextAware {

	public Object react(Action action) throws Exception {
		return action.execute();
	}

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

	}
}
