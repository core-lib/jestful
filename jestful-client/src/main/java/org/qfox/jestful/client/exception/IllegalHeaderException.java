package org.qfox.jestful.client.exception;

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
 * @date 2016年4月28日 下午7:16:51
 * @since 1.0.0
 */
public class IllegalHeaderException extends JestfulException {
    private static final long serialVersionUID = -6603706228883690629L;

    private final String header;

    public IllegalHeaderException(String message, String header) {
        super(message);
        this.header = header;
    }

    public String getHeader() {
        return header;
    }

}
