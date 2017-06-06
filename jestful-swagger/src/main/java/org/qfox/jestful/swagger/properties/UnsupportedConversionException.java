package org.qfox.jestful.swagger.properties;

import org.qfox.jestful.core.exception.JestfulException;

import java.lang.reflect.Type;

/**
 * Created by yangchangpei on 17/6/6.
 */
public class UnsupportedConversionException extends JestfulException {
    private static final long serialVersionUID = 3217669110684728060L;

    private final Type type;

    public UnsupportedConversionException(Type type) {
        this("can not make conversion for type " + type, type);
    }

    public UnsupportedConversionException(String message, Throwable cause, Type type) {
        super(message, cause);
        this.type = type;
    }

    public UnsupportedConversionException(String message, Type type) {
        super(message);
        this.type = type;
    }

    public UnsupportedConversionException(Throwable cause, Type type) {
        super(cause);
        this.type = type;
    }
}
