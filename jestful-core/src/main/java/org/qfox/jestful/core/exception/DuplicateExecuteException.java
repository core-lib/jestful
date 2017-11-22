package org.qfox.jestful.core.exception;

import org.qfox.jestful.core.Action;

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
public class DuplicateExecuteException extends JestfulException {
    private static final long serialVersionUID = 1099398331590683221L;

    private final Action action;

    public DuplicateExecuteException(Action action) {
        super("please ensure your actor does not execute an action more than once");
        this.action = action;
    }

    public Action getAction() {
        return action;
    }

}
