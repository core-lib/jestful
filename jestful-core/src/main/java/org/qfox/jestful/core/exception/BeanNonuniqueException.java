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
 * @date 2016年4月20日 下午12:20:31
 * @since 1.0.0
 */
public class BeanNonuniqueException extends JestfulRuntimeException {
    private static final long serialVersionUID = 1798195452994628452L;

    private final Class<?> type;
    private final BeanContainer beanContainer;

    public BeanNonuniqueException(Class<?> type, BeanContainer beanContainer) {
        super("bean typeof " + type + " is nonunique in container " + beanContainer);
        this.type = type;
        this.beanContainer = beanContainer;
    }

    public Class<?> getType() {
        return type;
    }

    public BeanContainer getBeanContainer() {
        return beanContainer;
    }

}
