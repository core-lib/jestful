package org.qfox.jestful.core;

import org.qfox.jestful.core.exception.BeanConfigException;

import java.util.List;
import java.util.Map;

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
 * @date 2016年5月14日 上午11:21:08
 * @since 1.0.0
 */
public class Component extends Group implements Plugin {

    public Component() {
    }

    public Component(List<Actor> members) {
        super(members);
    }

    public void config(Map<String, String> arguments) throws BeanConfigException {
        for (Actor member : members) {
            if (member instanceof Configurable) {
                Configurable configurable = (Configurable) member;
                configurable.config(arguments);
            }
        }
    }

}
