package org.qfox.jestful.client.scheduler;

import org.qfox.jestful.client.Client;
import org.qfox.jestful.client.exception.UncertainBodyTypeException;
import org.qfox.jestful.core.Action;

import java.lang.reflect.Type;

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
 * @date 2016年5月6日 上午11:49:18
 * @since 1.0.0
 */
public interface Scheduler {

    boolean supports(Action action);

    Type getBodyType(Client client, Action action) throws UncertainBodyTypeException;

    Object schedule(Client client, Action action) throws Exception;

}
