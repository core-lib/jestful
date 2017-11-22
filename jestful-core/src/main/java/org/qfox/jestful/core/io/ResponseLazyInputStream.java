package org.qfox.jestful.core.io;

import org.qfox.jestful.core.Response;

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
 * @date 2016年5月10日 下午5:52:27
 * @since 1.0.0
 */
public class ResponseLazyInputStream extends LazyInputStream {
    private final Response response;

    public ResponseLazyInputStream(Response response) {
        super();
        this.response = response;
    }

    @Override
    protected InputStream getInputStream() throws IOException {
        return response.getResponseInputStream();
    }

}
