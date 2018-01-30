package org.qfox.jestful.core.exception;

import org.qfox.jestful.core.Resource;

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
public class AmbiguousResourceException extends IllegalConfigException {
    private static final long serialVersionUID = 6106792803333438394L;

    private final Resource resource;

    public AmbiguousResourceException(String message, Object controller, Resource resource) {
        super(message, controller);
        this.resource = resource;
    }

    public Resource getResource() {
        return resource;
    }

}
