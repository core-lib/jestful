package org.qfox.jestful.android;

/**
 * 安卓主线程回调监听器, 后台线程进行网络调用返回后{@link UICallbackScheduler}会通过androi的主线程回调该监听器的对应方法
 * 
 * @author Administrator
 *
 * @param <R>
 */
public interface UICallback<R> {

	UICallback<Object> NULL = new UICallback<Object>() {

		public void onCompleted(boolean success, Object result, Exception exception) {

		}

		public void onSuccess(Object result) {

		}

		public void onFail(Exception exception) {

		}

	};

	/**
	 * 当处理完成时回调,即无论是否成功都会回调
	 * 
	 * @param success
	 *            true:处理成功 false:处理失败
	 * @param result
	 *            请求结果
	 * @param exception
	 *            失败异常
	 */
	void onCompleted(boolean success, R result, Exception exception);

	/**
	 * 成功时回调
	 * 
	 * @param result
	 *            调用结果
	 */
	void onSuccess(R result);

	/**
	 * 失败时回调
	 * 
	 * @param exception
	 *            失败异常
	 */
	void onFail(Exception exception);

}
