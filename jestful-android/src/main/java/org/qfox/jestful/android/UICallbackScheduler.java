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

public class UICallbackScheduler implements Scheduler {

    public boolean supports(Action action) {
        Parameters parameters = action.getParameters();
        Result result = action.getResult();
        return parameters.count(UICallback.class) == 1 && result.getKlass() == Void.TYPE;
    }

    public Type getBodyType(Client client, Action action) throws UncertainBodyTypeException {
        Parameters parameters = action.getParameters();
        Parameter parameter = parameters.unique(UICallback.class);
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
        Parameter parameter = parameters.unique(UICallback.class);
        @SuppressWarnings("unchecked")
        UICallback callback = parameter.getValue() != null ? (UICallback) parameter.getValue() : UICallback.NULL;
        new UICallbackTask(action, callback).execute();
        return null;
    }

}
