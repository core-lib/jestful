package org.qfox.jestful.core;

import org.qfox.jestful.core.annotation.Function;

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
 * @date 2016年4月13日 上午11:56:47
 * @since 1.0.0
 */
public class Restful {
    private final String method;
    private final boolean acceptBody;
    private final boolean returnBody;
    private final boolean idempotent;

    public Restful(Function function) {
        this.method = function.name();
        this.acceptBody = function.acceptBody();
        this.returnBody = function.returnBody();
        this.idempotent = function.idempotent();
    }

    public Restful(String method, boolean acceptBody, boolean returnBody, boolean idempotent) {
        super();
        this.method = method;
        this.acceptBody = acceptBody;
        this.returnBody = returnBody;
        this.idempotent = idempotent;
    }

    public String getMethod() {
        return method;
    }

    public boolean isAcceptBody() {
        return acceptBody;
    }

    public boolean isReturnBody() {
        return returnBody;
    }

    public boolean isIdempotent() {
        return idempotent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Restful restful = (Restful) o;

        return method != null ? method.equals(restful.method) : restful.method == null;
    }

    @Override
    public int hashCode() {
        return method != null ? method.hashCode() : 0;
    }

    public String toString() {
        return String.valueOf(method);
    }
}
