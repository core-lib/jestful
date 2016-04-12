package org.qfox.jestful.server.render;

import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Actor;
import org.qfox.jestful.core.Location;
import org.qfox.jestful.core.Response;
import org.qfox.jestful.core.Result;
import org.qfox.jestful.core.Status;
import org.qfox.jestful.server.exception.ServerStatusException;

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
 * @date 2016年4月12日 下午8:21:45
 *
 * @since 1.0.0
 */
public class StatusResultRender implements Actor {

	public Object react(Action action) throws Exception {
		Result result = action.getResult();
		if (result.getLocation() != Location.STATUS) {
			return action.execute();
		}

		Response response = action.getResponse();
		Object value = result.getValue();

		if (value instanceof Number) {
			Number code = (Number) value;
			Status status = new Status(code.intValue());
			response.setStatus(status);
		} else if (value instanceof Status) {
			Status status = (Status) value;
			response.setStatus(status);
		} else {
			throw new ServerStatusException("Unknown Status");
		}

		return action.execute();
	}

}
