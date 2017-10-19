package org.qfox.jestful.client.scheduler;

import org.qfox.jestful.client.ActionFuture;
import org.qfox.jestful.client.Client;
import org.qfox.jestful.client.Promise;
import org.qfox.jestful.client.exception.UncertainBodyTypeException;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Result;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.concurrent.Future;

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
 * @date 2016年5月6日 下午9:39:44
 * @since 1.0.0
 */
public class FutureScheduler implements Scheduler {

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
            return parameterizedType.getActualTypeArguments()[0];
        } else {
            throw new UncertainBodyTypeException(client, action);
        }
    }

    public Object schedule(final Client client, final Action action) throws Exception {
        ActionFuture future = new ActionFuture(action);
        Promise promise = (Promise) action.execute();
        promise.get(new FutureCallback(future));
        return future;
    }

    private static class FutureCallback extends CallbackAdapter<Object> {
        private final ActionFuture future;

        FutureCallback(ActionFuture future) {
            this.future = future;
        }

        @Override
        public void onCompleted(boolean success, Object result, Exception exception) {
            future.done();
        }
    }

}
