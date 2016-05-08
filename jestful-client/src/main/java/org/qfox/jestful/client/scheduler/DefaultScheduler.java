package org.qfox.jestful.client.scheduler;

import java.lang.reflect.Type;

import org.qfox.jestful.client.Client;
import org.qfox.jestful.client.exception.UncertainReturnTypeException;
import org.qfox.jestful.core.Action;
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
 * @date 2016年5月6日 上午11:54:21
 *
 * @since 1.0.0
 */
public class DefaultScheduler implements Scheduler {

	public boolean supports(Action action) {
		return true;
	}

	public Type getBodyType(Client client, Action action) throws UncertainReturnTypeException {
		Result result = action.getResult();
		return result.getReturnType();
	}

	public Object schedule(Client client, Action action) throws Exception {
		return action.execute();
	}

}
