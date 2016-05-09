package org.qfox.jestful.client.scheduler;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.qfox.jestful.client.Client;
import org.qfox.jestful.client.exception.UncertainBodyTypeException;
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
 * @date 2016年5月6日 下午9:39:44
 *
 * @since 1.0.0
 */
public class FutureScheduler implements Scheduler {
	private int maxThreadSize = 24;
	private ExecutorService executor = Executors.newFixedThreadPool(maxThreadSize);

	public boolean supports(Action action) {
		Result result = action.getResult();
		Class<?> klass = result.getKlass();
		return klass == Future.class;
	}

	public Type getBodyType(Client client, Action action) throws UncertainBodyTypeException {
		Result result = action.getResult();
		Type type = result.getType();
		if (type instanceof ParameterizedType) {
			ParameterizedType parameterizedType = (ParameterizedType) type;
			Type actualTypeArgument = parameterizedType.getActualTypeArguments()[0];
			return actualTypeArgument;
		} else {
			throw new UncertainBodyTypeException(client, action);
		}
	}

	public Object schedule(final Client client, final Action action) throws Exception {
		Future<Object> future = executor.submit(new Callable<Object>() {

			public Object call() throws Exception {
				return action.execute();
			}

		});
		return future;
	}

	public int getMaxThreadSize() {
		return maxThreadSize;
	}

	public void setMaxThreadSize(int maxThreadSize) {
		if (maxThreadSize <= 0) {
			throw new IllegalArgumentException("max thread size should greater than zero");
		}
		this.maxThreadSize = maxThreadSize;
		this.executor.shutdown();
		this.executor = Executors.newFixedThreadPool(maxThreadSize);
	}

}
