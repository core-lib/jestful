package org.qfox.jestful.swagger.properties;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.properties.FileProperty;
import io.swagger.models.properties.Property;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.lang.reflect.Type;

/**
 * Created by yangchangpei on 17/6/6.
 */
public class FilePropertyConverter extends AbstractPropertyConverter implements PropertyConverter {

    @Override
    public boolean supports(Type type) {
        return MultipartFile.class == type || File.class == type;
    }

    @Override
    public Property convert(Type type, ApiModelProperty property, PropertyConversionProvider provider) throws Exception {
        Property p = new FileProperty();

        convert(property, p);

        return p;
    }
}
