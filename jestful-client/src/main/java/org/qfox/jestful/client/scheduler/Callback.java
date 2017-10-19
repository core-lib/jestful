package org.qfox.jestful.client.scheduler;

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
 * @date 2016年5月6日 下午9:39:58
 * @since 1.0.0
 */
public interface Callback<R> {

    Callback<Object> DEFAULT = new CallbackAdapter<Object>() {
    };

    /**
     * 当处理完成时回调,即无论是否成功都会回调
     *
     * @param success   true:处理成功 false:处理失败
     * @param result    请求结果
     * @param exception 失败异常
     */
    void onCompleted(boolean success, R result, Exception exception);

    /**
     * 成功时回调
     *
     * @param result 调用结果
     */
    void onSuccess(R result);

    /**
     * 失败时回调
     *
     * @param exception 失败异常
     */
    void onFail(Exception exception);

}
