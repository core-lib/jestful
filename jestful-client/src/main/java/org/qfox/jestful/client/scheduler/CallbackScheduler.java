package org.qfox.jestful.client.scheduler;

import org.qfox.jestful.client.Client;
import org.qfox.jestful.client.exception.UncertainBodyTypeException;
import org.qfox.jestful.core.*;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
 * @date 2016年5月6日 下午12:01:41
 * @since 1.0.0
 */
public class CallbackScheduler implements Scheduler, Destroyable {
    protected int maxThreadSize = 24;
    protected ExecutorService executor = Executors.newFixedThreadPool(maxThreadSize);

    public boolean supports(Action action) {
        Parameters parameters = action.getParameters();
        Result result = action.getResult();
        return parameters.count(Callback.class) == 1 && result.getKlass() == Void.TYPE;
    }

    public Type getBodyType(Client client, Action action) throws UncertainBodyTypeException {
        Parameters parameters = action.getParameters();
        Parameter parameter = parameters.unique(Callback.class);
        Type type = parameter.getType();
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            return parameterizedType.getActualTypeArguments()[0];
        } else {
            throw new UncertainBodyTypeException(client, action);
        }
    }

    public Object schedule(final Client client, final Action action) throws Exception {
        final Parameters parameters = action.getParameters();
        final Parameter parameter = parameters.unique(Callback.class);
        @SuppressWarnings("unchecked")
        final Callback<Object> callback = parameter.getValue() != null ? (Callback<Object>) parameter.getValue() : Callback.NULL;
        executor.execute(new Runnable() {

            public void run() {
                Object result = null;
                Throwable throwable = null;
                try {
                    result = action.execute();
                    callback.onSuccess(result);
                } catch (Throwable t) {
                    throwable = t;
                    callback.onFail(throwable);
                } finally {
                    callback.onCompleted(throwable == null, result, throwable);
                }
            }

        });
        return null;
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

    @Override
    public void destroy() {
        executor.shutdown();
    }
}
