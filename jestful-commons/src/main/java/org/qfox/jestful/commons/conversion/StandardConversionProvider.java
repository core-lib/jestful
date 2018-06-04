package org.qfox.jestful.commons.conversion;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 标准的转换提供器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-06-04 13:47
 **/
public class StandardConversionProvider implements ConversionProvider {
    private final List<Converter> converters;

    public StandardConversionProvider(Collection<Converter> converters) {
        if (converters == null) throw new NullPointerException();
        this.converters = new ArrayList<Converter>(converters);
    }

    @Override
    public Object convert(Conversion conversion) throws ConvertingException {
        try {
            for (Converter<?> converter : converters) if (converter.supports(conversion)) return converter.convert(conversion, this);

            Class<?> clazz = conversion.type instanceof Class<?>
                    ? (Class<?>) conversion.type
                    : conversion.type instanceof ParameterizedType && ((ParameterizedType) conversion.type).getRawType() instanceof Class<?>
                    ? (Class<?>) ((ParameterizedType) conversion.type).getRawType()
                    : null;

            if (clazz == null) return conversion.value;

            if (conversion.name.isEmpty()) {
                String expression = conversion.expression;
                int i1 = expression.indexOf(".");
                int i2 = expression.indexOf("[");
                int i = i1 < 0 && i2 < 0 ? -1 : Math.min(i1, i2);
                String field = i < 0 ? expression : expression.substring(0, i);
                field = conversion.decoded ? field : URLDecoder.decode(field, conversion.charset);
                PropertyDescriptor descriptor = new PropertyDescriptor(field, clazz);
                Method getter = descriptor.getReadMethod();
                Method setter = descriptor.getWriteMethod();
                if (getter == null || setter == null) return conversion.value;
                Object instance = conversion.value != null ? conversion.value : clazz.newInstance();
                Class<?> type = getter.getReturnType();
                Object value = getter.invoke(instance);
                Conversion cvs = new Conversion(field, value, type, conversion.decoded, conversion.charset, expression, conversion.values);
                value = this.convert(cvs);
                setter.invoke(instance, value);
                return instance;
            } else if (conversion.expression.startsWith(conversion.name + ".")) {
                String expression = conversion.expression.substring(conversion.name.length() + 1);
                int i1 = expression.indexOf(".");
                int i2 = expression.indexOf("[");
                int i = i1 * i2 >= 0 ? Math.min(i1, i2) : Math.max(i1, i2);
                String field = i < 0 ? expression : expression.substring(0, i);
                field = conversion.decoded ? field : URLDecoder.decode(field, conversion.charset);
                PropertyDescriptor descriptor = new PropertyDescriptor(field, clazz);
                Method getter = descriptor.getReadMethod();
                Method setter = descriptor.getWriteMethod();
                if (getter == null || setter == null) return conversion.value;
                Object instance = conversion.value != null ? conversion.value : clazz.newInstance();
                Class<?> type = getter.getReturnType();
                Object value = getter.invoke(instance);
                Conversion cvs = new Conversion(field, value, type, conversion.decoded, conversion.charset, expression, conversion.values);
                value = this.convert(cvs);
                setter.invoke(instance, value);
                return instance;
            } else if (conversion.expression.startsWith(conversion.name + "['")) {
                String expression = conversion.expression.substring(conversion.name.length());
                int i = expression.indexOf("']");
                if (i < 0) return conversion.value;
                String field = expression.substring(2, i);
                field = conversion.decoded ? field : URLDecoder.decode(field, conversion.charset);
                PropertyDescriptor descriptor = new PropertyDescriptor(field, clazz);
                Method getter = descriptor.getReadMethod();
                Method setter = descriptor.getWriteMethod();
                if (getter == null || setter == null) return conversion.value;
                Object instance = conversion.value != null ? conversion.value : clazz.newInstance();
                Class<?> type = getter.getReturnType();
                Object value = getter.invoke(instance, field);
                Conversion cvs = new Conversion("['" + field + "']", value, type, conversion.decoded, conversion.charset, expression, conversion.values);
                value = this.convert(cvs);
                setter.invoke(instance, value);
                return instance;
            } else {
                return conversion.value;
            }
        } catch (Exception e) {
            throw new ConvertingException(conversion);
        }
    }

}
