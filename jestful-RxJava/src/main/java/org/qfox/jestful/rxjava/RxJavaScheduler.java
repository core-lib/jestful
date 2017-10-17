package org.qfox.jestful.rxjava;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.qfox.jestful.client.Client;
import org.qfox.jestful.client.exception.UncertainBodyTypeException;
import org.qfox.jestful.client.scheduler.Scheduler;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Result;

import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Subscriber;

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
 * @date 2016年6月3日 上午9:55:23
 *
 * @since 1.0.0
 */
public class RxJavaScheduler implements Scheduler {

	public boolean supports(Action action) {
		Result result = action.getResult();
		Class<?> klass = result.getKlass();
		return klass == Observable.class;
	}

	public Type getBodyType(Client client, Action action) throws UncertainBodyTypeException {
		Result result = action.getResult();
		Type type = result.getType();
		if (type instanceof ParameterizedType) {
			ParameterizedType parameterizedType = (ParameterizedType) type;
			return parameterizedType.getActualTypeArguments()[0];
		} else {
			throw new UncertainBodyTypeException(client, action);
		}
	}

	public Object schedule(final Client client, final Action action) throws Exception {
		Observable<Object> observable = Observable.create(new OnSubscribe<Object>() {

			public void call(Subscriber<? super Object> subscriber) {
				try {
					Object value = action.execute();
					subscriber.onNext(value);
				} catch (Throwable e) {
					subscriber.onError(e);
				} finally {
					subscriber.onCompleted();
				}
			}

		});
		return observable;
	}

}
