package org.qfox.jestful.android;

import org.qfox.jestful.client.Client;
import org.qfox.jestful.client.exception.UncertainBodyTypeException;
import org.qfox.jestful.client.scheduler.Scheduler;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Parameter;
import org.qfox.jestful.core.Parameters;
import org.qfox.jestful.core.Result;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

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
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            return parameterizedType.getActualTypeArguments()[0];
        } else {
            throw new UncertainBodyTypeException(client, action);
        }
    }

    public Object schedule(Client client, Action action) throws Exception {
        Parameters parameters = action.getParameters();
        Parameter parameter = parameters.unique(Callback.class);
        @SuppressWarnings("unchecked")
        Callback callback = parameter.getValue() != null ? (Callback) parameter.getValue() : Callback.NULL;
        new CallbackTask(action, callback).execute();
        return null;
    }

}
