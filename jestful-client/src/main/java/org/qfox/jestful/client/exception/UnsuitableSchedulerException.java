package org.qfox.jestful.client.exception;

import org.qfox.jestful.client.scheduler.Scheduler;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.exception.JestfulException;

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
 * @date 2016年5月6日 下午12:20:01
 *
 * @since 1.0.0
 */
public class UnsuitableSchedulerException extends JestfulException {
	private static final long serialVersionUID = -7439275877038555301L;

	private final Action action;
	private final Scheduler scheduler;

	public UnsuitableSchedulerException(Action action, Scheduler scheduler) {
		super();
		this.action = action;
		this.scheduler = scheduler;
	}

	public Action getAction() {
		return action;
	}

	public Scheduler getScheduler() {
		return scheduler;
	}

}
