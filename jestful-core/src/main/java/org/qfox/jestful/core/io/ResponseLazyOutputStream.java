package org.qfox.jestful.core.io;

import org.qfox.jestful.core.Response;

import java.io.IOException;
import java.io.OutputStream;

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
 * @date 2016年5月10日 下午5:53:14
 * @since 1.0.0
 */
public class ResponseLazyOutputStream extends LazyOutputStream {
    private final Response response;

    public ResponseLazyOutputStream(Response response) {
        super();
        this.response = response;
    }

    @Override
    protected OutputStream getOutputStream() throws IOException {
        return response.getResponseOutputStream();
    }

}
