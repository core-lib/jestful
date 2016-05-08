package org.qfox.jestful.client.exception;

import java.util.Map;

import org.qfox.jestful.client.scheduler.Scheduler;
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

	private final String name;
	private final Map<String, Scheduler> schedulers;

	public NoSuchSchedulerException(String name, Map<String, Scheduler> schedulers) {
		super();
		this.name = name;
		this.schedulers = schedulers;
	}

	public String getName() {
		return name;
	}

	public Map<String, Scheduler> getSchedulers() {
		return schedulers;
	}

}
