package org.qfox.jestful.swagger.models;

import io.swagger.models.Model;

import java.lang.reflect.Type;

/**
 * Created by yangchangpei on 17/6/6.
 */
public interface ModelConverter {

    boolean supports(Type type);

    Model convert(Type type, ModelConversionProvider provider) throws Exception;

}
