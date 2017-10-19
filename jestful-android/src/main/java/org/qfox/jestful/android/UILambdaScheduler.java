package org.qfox.jestful.android;

import org.qfox.jestful.client.Client;
import org.qfox.jestful.client.exception.UncertainBodyTypeException;
import org.qfox.jestful.client.scheduler.Scheduler;
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
public class UILambdaScheduler implements Scheduler, Destroyable {
    protected ExecutorService executor = Executors.newCachedThreadPool();

    @Override
    public boolean supports(Action action) {
        Parameters parameters = action.getParameters();
        Result result = action.getResult();
        return parameters.count(UIOnLambda.class) > 0 && result.getKlass() == Void.TYPE;
    }

    @Override
    public Type getBodyType(Client client, Action action) throws UncertainBodyTypeException {
        Parameters parameters = action.getParameters();
        List<Parameter> params = parameters.all(UIOnLambda.class);
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
        Parameter success = parameters.first(UIOnSuccess.class);
        UIOnSuccess onSuccess = success != null && success.getValue() != null ? (UIOnSuccess) success.getValue() : UIOnSuccess.DEFAULT;
        Parameter fail = parameters.first(UIOnFail.class);
        UIOnFail onFail = fail != null && fail.getValue() != null ? (UIOnFail) fail.getValue() : UIOnFail.DEFAULT;
        Parameter completed = parameters.first(UIOnCompleted.class);
        UIOnCompleted onCompleted = completed != null && completed.getValue() != null ? (UIOnCompleted) completed.getValue() : UIOnCompleted.DEFAULT;
        new UIOnLambdaTask(action, onSuccess, onFail, onCompleted).execute();
        return null;
    }

    @Override
    public void destroy() {
        executor.shutdown();
    }
}
