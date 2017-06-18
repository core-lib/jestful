package org.qfox.jestful.swagger;

import io.swagger.annotations.SwaggerDefinition;

import java.util.Map;

/**
 * Created by yangchangpei on 17/6/2.
 */
public class SwaggerDefinitionNotUniqueException extends RuntimeException {
    private static final long serialVersionUID = 7124060696736063538L;

    public SwaggerDefinitionNotUniqueException(Map<String, Object> definitions) {
        this("find more than one class with annotation " + SwaggerDefinition.class.getName() + " " + definitions);
    }

    public SwaggerDefinitionNotUniqueException(String message) {
        super(message);
    }

    public SwaggerDefinitionNotUniqueException(String message, Throwable cause) {
        super(message, cause);
    }

    public SwaggerDefinitionNotUniqueException(Throwable cause) {
        super(cause);
    }
}
