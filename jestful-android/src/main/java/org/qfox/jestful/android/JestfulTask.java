package org.qfox.jestful.android;

import org.qfox.jestful.core.Action;

import android.os.AsyncTask;

public class JestfulTask extends AsyncTask<Object, Integer, Object> {
	private final Action action;
	private final Listener<Object> listener;
	private Throwable throwable;

	public JestfulTask(Action action, Listener<Object> listener) {
		super();
		this.action = action;
		this.listener = listener;
	}

	@Override
	protected Object doInBackground(Object... parameters) {
		try {
			return action.execute();
		} catch (Throwable e) {
			throwable = e;
			return null;
		}
	}

	@Override
	protected void onPostExecute(Object result) {
		try {
			if (throwable == null) {
				listener.onSuccess(result);
			} else {
				listener.onFail(throwable);
			}
		} finally {
			listener.onCompleted(throwable == null, result, throwable);
		}
	}

}
