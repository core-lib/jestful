package org.qfox.jestful.server.exception;

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
 * @date 2016年3月31日 上午10:32:37
 * @since 1.0.0
 */
public class NotFoundStatusException extends StatusException {
    private static final long serialVersionUID = 7507138827736751227L;

    private final String version;

    public NotFoundStatusException(String uri, String method, String version) {
        super(uri, method, 404, "Not Found");
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

}
