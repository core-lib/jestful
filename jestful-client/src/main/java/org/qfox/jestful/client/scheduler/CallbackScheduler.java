package org.qfox.jestful.client.scheduler;

import org.qfox.jestful.client.Client;
import org.qfox.jestful.client.Promise;
import org.qfox.jestful.client.exception.UncertainBodyTypeException;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Parameter;
import org.qfox.jestful.core.Parameters;
import org.qfox.jestful.core.Result;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

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
public class CallbackScheduler implements Scheduler {

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
        Parameters parameters = action.getParameters();
        Parameter parameter = parameters.unique(Callback.class);
        Callback callback = parameter.getValue() != null ? (Callback) parameter.getValue() : Callback.DEFAULT;
        Promise promise = (Promise) action.execute();
        promise.observe(callback);
        return null;
    }

}
