package org.qfox.jestful.server.obtainer;

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
 * @date 2016年4月19日 下午3:54:05
 * @since 1.0.0
 */
public class RequestParameterObtainer implements Obtainer {

    public Object obtain(Action action, Parameter parameter) {
        if (parameter.getKlass().isInstance(action.getRequest())) {
            return action.getRequest();
        }
        return null;
    }

}
