package org.qfox.jestful.client.scheduler;

import org.qfox.jestful.core.Action;

/**
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 * 
 * @author Payne 646742615@qq.com
 *
 * @date 2016年5月6日 上午11:49:18
 *
 * @since 1.0.0
 */
public interface Scheduler {

	boolean supports(Action action);

	Object schedule(Action action) throws Exception;

}
