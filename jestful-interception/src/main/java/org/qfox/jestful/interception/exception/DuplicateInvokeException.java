package org.qfox.jestful.interception.exception;

import org.qfox.jestful.core.exception.JestfulException;
import org.qfox.jestful.interception.Invocation;

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
 * @date 2016年2月22日 下午4:35:22
 * @since 1.0.0
 */
public class DuplicateInvokeException extends JestfulException {
    private static final long serialVersionUID = 1099398331590683221L;

    private final Invocation invocation;

    public DuplicateInvokeException(Invocation invocation) {
        super("please ensure your interceptor does not invoke an invocation more than once");
        this.invocation = invocation;
    }

    public Invocation getInvocation() {
        return invocation;
    }
}
