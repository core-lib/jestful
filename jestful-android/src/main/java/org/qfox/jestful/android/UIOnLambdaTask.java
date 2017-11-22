package org.qfox.jestful.android;

import android.os.AsyncTask;
import org.qfox.jestful.client.Promise;
import org.qfox.jestful.client.scheduler.CallbackAdapter;
import org.qfox.jestful.client.scheduler.Calling;
import org.qfox.jestful.core.Action;

/**
 * Created by payne on 2017/4/18.
 * Version: 1.0
 */
class UIOnLambdaTask extends AsyncTask<Object, Integer, Object> {
    private final Action action;
    private final UIOnSuccess onSuccess;
    private final UIOnFail onFail;
    private final UIOnCompleted onCompleted;
    private Exception ex;

    UIOnLambdaTask(Action action, UIOnSuccess onSuccess, UIOnFail onFail, UIOnCompleted onCompleted) {
        this.action = action;
        this.onSuccess = onSuccess;
        this.onFail = onFail;
        this.onCompleted = onCompleted;
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
                            UILambdaDecorator<Object> decorator = new UILambdaDecorator<Object>(onSuccess, onFail, onCompleted);
                            new Calling(decorator, result, exception).call();
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
            onFail.call(ex = e);
        } finally {
            if (ex != null) onCompleted.call(false, null, ex);
        }
    }

}
