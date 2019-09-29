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
    public static final Restful GET = new Restful(org.qfox.jestful.core.http.GET.class.getAnnotation(Function.class));
    public static final Restful POST = new Restful(org.qfox.jestful.core.http.POST.class.getAnnotation(Function.class));
    public static final Restful PUT = new Restful(org.qfox.jestful.core.http.PUT.class.getAnnotation(Function.class));
    public static final Restful DELETE = new Restful(org.qfox.jestful.core.http.DELETE.class.getAnnotation(Function.class));
    public static final Restful HEAD = new Restful(org.qfox.jestful.core.http.HEAD.class.getAnnotation(Function.class));
    public static final Restful OPTIONS = new Restful(org.qfox.jestful.core.http.OPTIONS.class.getAnnotation(Function.class));

    private final String method;
    private final boolean acceptBody;
    private final boolean returnBody;
    private final boolean idempotent;
    private final String handler;

    public Restful(Function function) {
        this.method = function.name();
        this.acceptBody = function.acceptBody();
        this.returnBody = function.returnBody();
        this.idempotent = function.idempotent();
        this.handler = function.handler();
    }

    public Restful(String method, boolean acceptBody, boolean returnBody, boolean idempotent, String handler) {
        this.method = method;
        this.acceptBody = acceptBody;
        this.returnBody = returnBody;
        this.idempotent = idempotent;
        this.handler = handler;
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

    public String getHandler() {
        return handler;
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
