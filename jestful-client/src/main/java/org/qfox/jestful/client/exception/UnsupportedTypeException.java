package org.qfox.jestful.client.exception;

import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.exception.JestfulException;

import java.lang.reflect.Type;

/**
 * 不支持的返回值类型异常
 *
 * @author Payne 646742615@qq.com
 * 2019/9/30 11:22
 */
public class UnsupportedTypeException extends JestfulException {
    private final Action action;
    private final String method;
    private final Type type;

    public UnsupportedTypeException(Action action, String method, Type type) {
        this.action = action;
        this.method = method;
        this.type = type;
    }

    public UnsupportedTypeException(String message, Throwable cause, Action action, String method, Type type) {
        super(message, cause);
        this.action = action;
        this.method = method;
        this.type = type;
    }

    public UnsupportedTypeException(String message, Action action, String method, Type type) {
        super(message);
        this.action = action;
        this.method = method;
        this.type = type;
    }

    public UnsupportedTypeException(Throwable cause, Action action, String method, Type type) {
        super(cause);
        this.action = action;
        this.method = method;
        this.type = type;
    }

    public Action getAction() {
        return action;
    }

    public String getMethod() {
        return method;
    }

    public Type getType() {
        return type;
    }
}
