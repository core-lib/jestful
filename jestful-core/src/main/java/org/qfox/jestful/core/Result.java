package org.qfox.jestful.core;

import org.qfox.jestful.core.exception.IllegalConfigException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

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
 * @date 2016年4月12日 下午7:31:24
 * @since 1.0.0
 */
public class Result extends Configuration {
    private final Mapping mapping;
    private final Object controller;
    private final Method method;
    private final Type type;
    private final Class<?> klass;
    private final Body body;
    private Object value;
    private Exception exception;
    private boolean rendered = false;

    public Result(Type type) {
        super(new Annotation[0]);
        this.mapping = null;
        this.controller = null;
        this.method = null;
        this.type = type;
        this.klass = type instanceof Class<?> ? (Class<?>) type : type instanceof ParameterizedType ? (Class<?>) ((ParameterizedType) type).getRawType() : null;
        this.body = new Body(type);
    }

    public Result(Mapping mapping, Method method) throws IllegalConfigException {
        super(method.getAnnotations());
        try {
            this.mapping = mapping;
            this.controller = mapping.getController();
            this.method = method;
            this.type = method.getGenericReturnType();
            this.klass = method.getReturnType();
            this.body = new Body(type);
        } catch (Exception e) {
            throw new IllegalConfigException(e, mapping.getController(), method);
        }
    }

    public Result reset() {
        value = null;
        exception = null;
        rendered = false;
        body.reset();
        return this;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public boolean isRendered() {
        return rendered;
    }

    public void setRendered(boolean rendered) {
        this.rendered = rendered;
    }

    public Mapping getMapping() {
        return mapping;
    }

    public Object getController() {
        return controller;
    }

    public Method getMethod() {
        return method;
    }

    public Type getType() {
        return type;
    }

    public Class<?> getKlass() {
        return klass;
    }

    public Body getBody() {
        return body;
    }

}
