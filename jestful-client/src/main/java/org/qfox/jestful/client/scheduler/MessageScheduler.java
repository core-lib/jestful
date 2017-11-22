package org.qfox.jestful.client.scheduler;

import org.qfox.jestful.client.Client;
import org.qfox.jestful.client.Message;
import org.qfox.jestful.client.Promise;
import org.qfox.jestful.client.exception.UncertainBodyTypeException;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Response;
import org.qfox.jestful.core.Result;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by yangchangpei on 17/3/13.
 */
public class MessageScheduler implements Scheduler {

    @Override
    public boolean supports(Action action) {
        Result result = action.getResult();
        Class<?> klass = result.getKlass();
        return klass == Message.class;
    }

    @Override
    public Type getBodyType(Client client, Action action) throws UncertainBodyTypeException {
        Result result = action.getResult();
        Type type = result.getType();
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type actualTypeArgument = parameterizedType.getActualTypeArguments()[0];
            return actualTypeArgument;
        } else {
            throw new UncertainBodyTypeException(client, action);
        }
    }

    @Override
    public Object schedule(Client client, Action action) throws Exception {
        try {
            Promise promise = (Promise) action.execute();
            Object entity = promise.acquire();
            Response response = action.getResponse();
            return new Message<Object>(response, entity);
        } catch (Exception exception) {
            Response response = action.getResponse();
            return new Message<Object>(response, exception);
        }
    }
}
