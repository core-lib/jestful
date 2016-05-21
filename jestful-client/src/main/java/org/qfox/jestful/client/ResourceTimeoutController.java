package org.qfox.jestful.client;

import java.util.concurrent.TimeUnit;

import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Actor;
import org.qfox.jestful.core.Request;
import org.qfox.jestful.core.Resource;
import org.qfox.jestful.core.annotation.Timeout;

public class ResourceTimeoutController implements Actor {

	public Object react(Action action) throws Exception {
		Resource resource = action.getResource();
		if (resource.isAnnotationPresent(Timeout.class)) {
			Timeout timeout = resource.getAnnotation(Timeout.class);
			int value = timeout.value();
			TimeUnit unit = timeout.unit();
			long duration = unit.toMillis(value);
			Request request = action.getRequest();
			request.setReadTimeout((int) duration);
		}
		return action.execute();
	}

}
