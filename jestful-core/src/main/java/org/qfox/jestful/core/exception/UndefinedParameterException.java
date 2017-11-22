package org.qfox.jestful.core.exception;

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
 * @date 2016年4月6日 下午8:15:17
 * @since 1.0.0
 */
public class UndefinedParameterException extends IllegalConfigException {
    private static final long serialVersionUID = 6155312748066193777L;

    private final String name;
    private final String path;

    public UndefinedParameterException(Object controller, Method method, String name, String path) {
        super("undefined parameter named " + name + " on path " + path + " in method " + method + " of controller " + controller, controller, method);
        this.name = name;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

}
