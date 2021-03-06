package org.qfox.jestful.rxandroid;

import org.qfox.jestful.client.Client;
import org.qfox.jestful.client.Promise;
import org.qfox.jestful.client.exception.UncertainBodyTypeException;
import org.qfox.jestful.client.scheduler.Scheduler;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Result;
import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Subscriber;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * <p>
 * Description:
 * </p>
 * <p>
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 *
 * @author Payne 646742615@qq.com
 * @date 2016年6月3日 上午11:17:18
 * @since 1.0.0
 */
public class RxAndroidScheduler implements Scheduler {

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
                    Promise promise = (Promise) action.execute();
                    Object value = promise.acquire();
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
