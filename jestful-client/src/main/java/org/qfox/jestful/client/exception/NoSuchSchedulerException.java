package org.qfox.jestful.client.exception;

import java.util.Map;

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
 * @date 2016年5月6日 下午12:13:43
 *
 * @since 1.0.0
 */
public class NoSuchSchedulerException extends JestfulException {
	private static final long serialVersionUID = 1247770737847092768L;

	private final Action action;
	private final Map<String, Scheduler> schedulers;

	public NoSuchSchedulerException(Action action, Map<String, Scheduler> schedulers) {
		super();
		this.action = action;
		this.schedulers = schedulers;
	}

	public Action getAction() {
		return action;
	}

	public Map<String, Scheduler> getSchedulers() {
		return schedulers;
	}

}
