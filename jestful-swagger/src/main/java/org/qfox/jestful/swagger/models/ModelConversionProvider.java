package org.qfox.jestful.swagger.models;

import io.swagger.models.Model;

import java.lang.reflect.Type;

/**
 * Created by yangchangpei on 17/6/6.
 */
public interface ModelConversionProvider {

    Model convert(Type type) throws Exception;

}
