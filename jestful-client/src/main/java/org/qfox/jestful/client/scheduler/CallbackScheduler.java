package org.qfox.jestful.client.scheduler;

import org.qfox.jestful.client.Client;
import org.qfox.jestful.client.exception.UncertainBodyTypeException;
import org.qfox.jestful.core.*;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
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
    protected ExecutorService executor = Executors.newCachedThreadPool();

    public boolean supports(Action action) {
        Parameters parameters = action.getParameters();
        Result result = action.getResult();
        return parameters.count(Callback.class) == 1 && result.getKlass() == Void.TYPE;
    }

    public Type getBodyType(Client client, Action action) throws UncertainBodyTypeException {
        Parameters parameters = action.getParameters();
        Parameter parameter = parameters.unique(Callback.class);
        Type type = parameter.getType();
        if (!(type instanceof ParameterizedType)) {
            throw new UncertainBodyTypeException(client, action);
        }
        ParameterizedType parameterizedType = (ParameterizedType) type;
        Type actualTypeArgument = parameterizedType.getActualTypeArguments()[0];
        if (!(actualTypeArgument instanceof TypeVariable<?>)) {
            return actualTypeArgument;
        }
        TypeVariable<?> typeVariable = (TypeVariable<?>) actualTypeArgument;
        Type[] bounds = typeVariable.getBounds();
        if (bounds.length == 0) {
            throw new UncertainBodyTypeException(client, action);
        }
        return bounds[0];
    }

    public Object schedule(final Client client, final Action action) throws Exception {
        executor.execute(new Runnable() {

            public void run() {
                Parameters parameters = action.getParameters();
                Parameter parameter = parameters.unique(Callback.class);
                Callback callback = parameter.getValue() != null ? (Callback) parameter.getValue() : Callback.DEFAULT;

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

    @Override
    public void destroy() {
        executor.shutdown();
    }
}
