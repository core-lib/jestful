package org.qfox.jestful.guava;

import com.google.common.util.concurrent.ListenableFuture;
import org.qfox.jestful.client.Client;
import org.qfox.jestful.client.Promise;
import org.qfox.jestful.client.exception.UncertainBodyTypeException;
import org.qfox.jestful.client.scheduler.CallbackAdapter;
import org.qfox.jestful.client.scheduler.Scheduler;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Result;

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
 * @date 2016年5月6日 下午9:39:44
 * @since 1.0.0
 */
public class ListenableFutureScheduler implements Scheduler {

    public boolean supports(Action action) {
        Result result = action.getResult();
        Class<?> klass = result.getKlass();
        return klass == ListenableFuture.class;
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
        ActionListenableFuture future = new ActionListenableFuture(action);
        Promise promise = (Promise) action.execute();
        promise.accept(new ListenableFutureCallback(future));
        return future;
    }

    private static class ListenableFutureCallback extends CallbackAdapter<Object> {
        private final ActionListenableFuture future;

        ListenableFutureCallback(ActionListenableFuture future) {
            this.future = future;
        }

        @Override
        public void onCompleted(boolean success, Object result, Exception exception) {
            future.done();
        }
    }

}
