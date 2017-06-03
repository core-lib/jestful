package org.qfox.jestful.swagger;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by yangchangpei on 17/6/3.
 */
public class SwaggerJsonMapper extends ObjectMapper {
    private static final long serialVersionUID = 8585175308902093421L;

    public SwaggerJsonMapper() {
        this.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }
}
