package org.qfox.jestful.core.exception;

import org.qfox.jestful.core.Mapping;

import java.lang.reflect.Method;

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
 * @date 2016年4月12日 上午11:31:29
 * @since 1.0.0
 */
public class AmbiguousMappingException extends IllegalConfigException {
    private static final long serialVersionUID = 6106792803333438394L;

    private final Mapping mapping;

    public AmbiguousMappingException(String message, Object controller, Method method, Mapping mapping) {
        super(message, controller, method);
        this.mapping = mapping;
    }

    public Mapping getMapping() {
        return mapping;
    }

}
