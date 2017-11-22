package org.qfox.jestful.core.exception;

import java.io.IOException;

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
 * @date 2016年6月2日 上午11:29:01
 * @since 1.0.0
 */
public class JestfulIOException extends IOException {
    private static final long serialVersionUID = 7963628178500645963L;

    public JestfulIOException() {
        super();
    }

    public JestfulIOException(String message, Throwable cause) {
        super(message);
        this.initCause(cause);
    }

    public JestfulIOException(String message) {
        super(message);
    }

    public JestfulIOException(Throwable cause) {
        super();
        this.initCause(cause);
    }

}
