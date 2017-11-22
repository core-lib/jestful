package org.qfox.jestful.core.exception;

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
 * @date 2016年5月14日 上午11:10:11
 * @since 1.0.0
 */
public class BeanConfigException extends JestfulRuntimeException {
    private static final long serialVersionUID = -342613651593818343L;

    public BeanConfigException() {
        super();
    }

    public BeanConfigException(String message, Throwable cause) {
        super(message, cause);
    }

    public BeanConfigException(String message) {
        super(message);
    }

    public BeanConfigException(Throwable cause) {
        super(cause);
    }

}
