package org.qfox.jestful.server.exception;

import org.qfox.jestful.core.Accepts;
import org.qfox.jestful.core.exception.StatusException;

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
 * @date 2016年4月9日 下午4:10:00
 * @since 1.0.0
 */
public class NotAcceptableStatusException extends StatusException {
    private static final long serialVersionUID = -3926715913399376714L;

    private final Accepts accepts;
    private final Accepts produces;

    public NotAcceptableStatusException(String uri, String method, Accepts accepts, Accepts produces) {
        super(uri, method, 406, "Not Acceptable");
        this.accepts = accepts;
        this.produces = produces;
    }

    public Accepts getAccepts() {
        return accepts;
    }

    public Accepts getProduces() {
        return produces;
    }

}
