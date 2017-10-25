package org.qfox.jestful.client.scheduler;

public class Calling {
    private final Callback<Object> callback;
    private final Object result;
    private final Exception exception;

    public Calling(Callback<Object> callback, Object result, Exception exception) {
        this.callback = callback;
        this.result = result;
        this.exception = exception;
    }

    public void call() {
        Exception ex = null;
        try {
            if (exception == null) callback.onSuccess(result);
            else throw exception;
        } catch (Exception e) {
            callback.onFail(ex = e);
        } finally {
            callback.onCompleted(ex == null, result, exception);
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
