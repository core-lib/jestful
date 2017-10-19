package org.qfox.jestful.client.scheduler;

import org.qfox.jestful.client.Client;
import org.qfox.jestful.client.Promise;
import org.qfox.jestful.client.exception.UncertainBodyTypeException;
import org.qfox.jestful.core.*;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by yangchangpei on 17/3/31.
 */
public class LambdaScheduler implements Scheduler {

    @Override
    public boolean supports(Action action) {
        Parameters parameters = action.getParameters();
        Result result = action.getResult();
        return parameters.count(OnLambda.class) > 0 && result.getKlass() == Void.TYPE;
    }

    @Override
    public Type getBodyType(Client client, Action action) throws UncertainBodyTypeException {
        Parameters parameters = action.getParameters();
        List<Parameter> params = parameters.all(OnLambda.class);
        for (Parameter param : params) {
            Type type = param.getType();
            if (!(type instanceof ParameterizedType)) continue;
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type actualTypeArgument = parameterizedType.getActualTypeArguments()[0];
            if (!(actualTypeArgument instanceof TypeVariable<?>)) return actualTypeArgument;
            TypeVariable<?> typeVariable = (TypeVariable<?>) actualTypeArgument;
            Type[] bounds = typeVariable.getBounds();
            if (bounds.length == 0) continue;
            return bounds[0];
        }
        throw new UncertainBodyTypeException(client, action);
    }

    @Override
    public Object schedule(final Client client, final Action action) throws Exception {
        Parameters parameters = action.getParameters();
        Parameter success = parameters.first(OnSuccess.class);
        final OnSuccess onSuccess = success != null && success.getValue() != null ? (OnSuccess) success.getValue() : OnSuccess.DEFAULT;
        Parameter fail = parameters.first(OnFail.class);
        final OnFail onFail = fail != null && fail.getValue() != null ? (OnFail) fail.getValue() : OnFail.DEFAULT;
        Parameter completed = parameters.first(OnCompleted.class);
        final OnCompleted onCompleted = completed != null && completed.getValue() != null ? (OnCompleted) completed.getValue() : OnCompleted.DEFAULT;
        Promise promise = (Promise) action.execute();
        promise.get(new Callback<Object>() {
            @Override
            public void onCompleted(boolean success, Object result, Exception exception) {
                onCompleted.call(success, result, exception);
            }

            @Override
            public void onSuccess(Object result) {
                onSuccess.call(result);
            }

            @Override
            public void onFail(Exception exception) {
                onFail.call(exception);
            }
        });
        return null;
    }

}
