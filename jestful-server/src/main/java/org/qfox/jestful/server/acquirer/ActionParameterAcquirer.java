package org.qfox.jestful.server.acquirer;

import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Parameter;

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
 * @date 2016年4月19日 下午4:00:36
 * @since 1.0.0
 */
public class ActionParameterAcquirer implements Acquirer {

    public Object acquire(Action action, Parameter parameter) {
        if (parameter.getKlass().isInstance(action)) {
            return action;
        }
        return null;
    }

}
