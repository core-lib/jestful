package org.qfox.jestful.swagger;

import io.swagger.annotations.SwaggerDefinition;

/**
 * Created by yangchangpei on 17/6/2.
 */
public class SwaggerDefinitionNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 8043982137596690855L;

    public SwaggerDefinitionNotFoundException() {
        this("can not find any swagger definition class which should annotated with " + SwaggerDefinition.class);
    }

    public SwaggerDefinitionNotFoundException(String message) {
        super(message);
    }

    public SwaggerDefinitionNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public SwaggerDefinitionNotFoundException(Throwable cause) {
        super(cause);
    }
}
