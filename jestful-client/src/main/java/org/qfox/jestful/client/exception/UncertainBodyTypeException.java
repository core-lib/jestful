package org.qfox.jestful.client.exception;

import org.qfox.jestful.client.Client;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.exception.JestfulException;

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
 * @date 2016年5月7日 下午3:54:29
 * @since 1.0.0
 */
public class UncertainBodyTypeException extends JestfulException {
    private static final long serialVersionUID = 376421971058848789L;

    private final Client client;
    private final Action action;

    public UncertainBodyTypeException(Client client, Action action) {
        super();
        this.client = client;
        this.action = action;
    }

    public Client getClient() {
        return client;
    }

    public Action getAction() {
        return action;
    }

}
