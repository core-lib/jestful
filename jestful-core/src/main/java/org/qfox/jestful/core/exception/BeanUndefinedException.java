package org.qfox.jestful.core.exception;

import org.qfox.jestful.core.BeanContainer;

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
 * @date 2016年4月20日 下午12:17:02
 * @since 1.0.0
 */
public class BeanUndefinedException extends JestfulRuntimeException {
    private static final long serialVersionUID = -4581187835688040709L;

    private final String name;
    private final Class<?> type;
    private final BeanContainer beanContainer;

    public BeanUndefinedException(String name, BeanContainer beanContainer) {
        super("bean named " + name + " is undefined in container " + beanContainer);
        this.name = name;
        this.type = null;
        this.beanContainer = beanContainer;
    }

    public BeanUndefinedException(Class<?> type, BeanContainer beanContainer) {
        super("bean typeof " + type + " is undefined in container " + beanContainer);
        this.name = null;
        this.type = type;
        this.beanContainer = beanContainer;
    }

    public String getName() {
        return name;
    }

    public Class<?> getType() {
        return type;
    }

    public BeanContainer getBeanContainer() {
        return beanContainer;
    }

}
