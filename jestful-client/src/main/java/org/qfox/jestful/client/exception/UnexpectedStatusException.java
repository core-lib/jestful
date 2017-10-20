package org.qfox.jestful.client.exception;

import org.qfox.jestful.core.Status;
import org.qfox.jestful.core.exception.StatusException;

import java.util.Map;

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
 * @date 2016年5月4日 下午8:39:56
 * @since 1.0.0
 */
public class UnexpectedStatusException extends StatusException {
    private static final long serialVersionUID = -3047547153816408719L;

    private final Map<String, String[]> header;
    private final String body;

    public UnexpectedStatusException(String uri, String method, Status status, Map<String, String[]> header, String body) {
        super(uri, method, status.getCode(), status.getReason());
        this.header = header;
        this.body = body;
    }

    public Map<String, String[]> getHeader() {
        return header;
    }

    public String getBody() {
        return body;
    }

}
