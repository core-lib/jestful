package org.qfox.jestful.swagger;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

/**
 * Created by yangchangpei on 17/6/3.
 */
public class SwaggerYamlMapper extends YAMLMapper {
    private static final long serialVersionUID = -7959822428108062253L;

    public SwaggerYamlMapper() {
        this.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }
}
