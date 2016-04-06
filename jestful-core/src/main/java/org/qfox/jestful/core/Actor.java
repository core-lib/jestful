package org.qfox.jestful.core;

/**
 * <p>
 * Description: The participant of RESTful action
 * </p>
 * 
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 * 
 * @author Payne 646742615@qq.com
 *
 * @date 2016年1月15日 下午8:54:19
 *
 * @since 1.0.0
 */
public interface Actor {

	/**
	 * do myself job for this action
	 * 
	 * @param action
	 *            the action to execute
	 * @return action result
	 * @throws Exception
	 *             all type exceptions may be thrown
	 */
	Object react(Action action) throws Exception;

}
