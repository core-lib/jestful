package org.qfox.jestful.android;

import android.os.AsyncTask;
import org.qfox.jestful.client.Promise;
import org.qfox.jestful.client.scheduler.Call;
import org.qfox.jestful.client.scheduler.CallbackAdapter;
import org.qfox.jestful.core.Action;

class UICallbackTask extends AsyncTask<Object, Integer, Object> {
    private final Action action;
    private final UICallback<Object> callback;
    private Exception ex;

    UICallbackTask(Action action, UICallback<Object> callback) {
        super();
        this.action = action;
        this.callback = callback;
    }

    @Override
    protected Object doInBackground(Object... parameters) {
        try {
            Promise promise = (Promise) action.execute();
            promise.accept(new CallbackAdapter<Object>() {
                @Override
                public void onCompleted(final boolean success, final Object result, final Exception exception) {
                    new AsyncTask<Object, Integer, Object>() {

                        @Override
                        protected Object doInBackground(Object... params) {
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Object nil) {
                            UICallbackDecorator<Object> decorator = new UICallbackDecorator<Object>(callback);
                            new Call(decorator, result, exception).call();
                        }

                    }.execute();
                }
            });
            return null;
        } catch (Exception e) {
            ex = e;
            return null;
        }
    }

    @Override
    protected void onPostExecute(Object nil) {
        try {
            if (ex != null) throw ex;
        } catch (Exception e) {
            callback.onFail(ex = e);
        } finally {
            if (ex != null) callback.onCompleted(false, null, ex);
        }
    }

}