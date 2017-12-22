package org.qfox.jestful.client.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Calling {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final Callback<Object> callback;
    private final Object result;
    private final Exception exception;

    public Calling(Callback<Object> callback, Object result, Exception exception) {
        this.callback = callback;
        this.result = result;
        this.exception = exception;
    }

    public void call() {
        try {
            Exception ex = null;
            try {
                if (exception == null) callback.onSuccess(result);
                else throw exception;
            } catch (Exception e) {
                callback.onFail(ex = e);
            } finally {
                callback.onCompleted(ex == null, result, exception);
            }
        } catch (Throwable throwable) {
            logger.error("exception thrown while calling the callback: " + callback, throwable);
        }
    }

    public Callback<Object> getCallback() {
        return callback;
    }

    public Object getResult() {
        return result;
    }

    public Exception getException() {
        return exception;
    }
}
