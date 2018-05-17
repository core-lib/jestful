package org.qfox.jestful.client.scheduler;

import org.qfox.jestful.client.Client;
import org.qfox.jestful.client.Promise;
import org.qfox.jestful.client.exception.UncertainBodyTypeException;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Result;
import org.qfox.jestful.core.exception.JestfulRuntimeException;

import java.lang.reflect.Method;
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
public class CompletableFutureScheduler implements Scheduler {
    private final static Class<?> COMPLETABLE_FUTURE_CLASS;
    private final static Method COMPLETE_SUCCESSFULLY_METHOD;
    private final static Method COMPLETE_EXCEPTIONALLY_METHOD;

    static {
        Class<?> completableFutureClass;
        Method completeSuccessfullyMethod;
        Method completeExceptionallyMethod;
        try {
            completableFutureClass = Class.forName("java.util.concurrent.CompletableFuture");
            completeSuccessfullyMethod = completableFutureClass.getMethod("complete", Object.class);
            completeExceptionallyMethod = completableFutureClass.getMethod("completeExceptionally", Throwable.class);
        } catch (Exception e) {
            completableFutureClass = null;
            completeSuccessfullyMethod = null;
            completeExceptionallyMethod = null;
        }
        COMPLETABLE_FUTURE_CLASS = completableFutureClass;
        COMPLETE_SUCCESSFULLY_METHOD = completeSuccessfullyMethod;
        COMPLETE_EXCEPTIONALLY_METHOD = completeExceptionallyMethod;
    }

    public boolean supports(Action action) {
        Result result = action.getResult();
        Class<?> klass = result.getKlass();
        return COMPLETABLE_FUTURE_CLASS != null && klass == COMPLETABLE_FUTURE_CLASS;
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
        Object future = COMPLETABLE_FUTURE_CLASS.newInstance();
        Promise promise = (Promise) action.execute();
        promise.accept(new CompletableFutureCallback(future));
        return future;
    }

    private static class CompletableFutureCallback extends CallbackAdapter<Object> {
        private final Object future;

        CompletableFutureCallback(Object future) {
            this.future = future;
        }

        @Override
        public void onCompleted(boolean success, Object result, Exception exception) {
            try {
                if (success) COMPLETE_SUCCESSFULLY_METHOD.invoke(future, result);
                else COMPLETE_EXCEPTIONALLY_METHOD.invoke(future, exception);
            } catch (JestfulRuntimeException e) {
                throw e;
            } catch (Exception e) {
                throw new JestfulRuntimeException(e);
            }
        }
    }

}
