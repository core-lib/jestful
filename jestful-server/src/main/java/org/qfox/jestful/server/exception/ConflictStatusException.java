package org.qfox.jestful.server.exception;

import org.qfox.jestful.core.exception.StatusException;

import java.util.Set;

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
 * @date 2016年6月2日 下午3:24:26
 * @since 1.0.0
 */
public class ConflictStatusException extends StatusException {
    private static final long serialVersionUID = -4584709951608518964L;

    private final Set<String> versions;

    public ConflictStatusException(String uri, String method, Set<String> versions) {
        super(uri, method, 409, "Conflict");
        this.versions = versions;
    }

    public Set<String> getVersions() {
        return versions;
    }

}
