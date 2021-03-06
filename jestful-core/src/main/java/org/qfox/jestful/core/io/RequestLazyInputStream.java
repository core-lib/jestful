package org.qfox.jestful.core.io;

import org.qfox.jestful.core.Request;

import java.io.IOException;
import java.io.InputStream;

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
 * @date 2016年5月10日 下午5:50:39
 * @since 1.0.0
 */
public class RequestLazyInputStream extends LazyInputStream {
    private final Request request;

    public RequestLazyInputStream(Request request) {
        super();
        this.request = request;
    }

    @Override
    protected InputStream getInputStream() throws IOException {
        request.connect();
        return request.getRequestInputStream();
    }

}
