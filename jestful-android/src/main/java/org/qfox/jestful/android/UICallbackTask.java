package org.qfox.jestful.android;

import android.os.AsyncTask;
import org.qfox.jestful.client.Promise;
import org.qfox.jestful.client.scheduler.CallbackAdapter;
import org.qfox.jestful.core.Action;

class UICallbackTask extends AsyncTask<Object, Integer, Object> {
    private final Action action;
    private final UICallback<Object> callback;
    private Exception exception;

    UICallbackTask(Action action, UICallback<Object> callback) {
        super();
        this.action = action;
        this.callback = callback;
    }

    @Override
    protected Object doInBackground(Object... parameters) {
        try {
            Promise promise = (Promise) action.execute();
            promise.observe(new CallbackAdapter<Object>() {
                @Override
                public void onCompleted(final boolean success, final Object result, final Exception exception) {
                    new AsyncTask<Object, Integer, Object>() {

                        @Override
                        protected Object doInBackground(Object... params) {
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Object nil) {
                            Exception ex = null;
                            try {
                                if (exception == null) callback.onSuccess(result);
                                else throw exception;
                            } catch (Exception e) {
                                callback.onFail(ex = e);
                            } finally {
                                callback.onCompleted(ex == null, result, ex);
                            }
                        }

                    }.execute();
                }
            });
            return null;
        } catch (Exception e) {
            exception = e;
            return null;
        }
    }

    @Override
    protected void onPostExecute(Object nil) {
        try {
            if (exception != null) throw exception;
        } catch (Exception e) {
            callback.onFail(e);
        } finally {
            if (exception != null) callback.onCompleted(false, null, exception);
        }
    }

}