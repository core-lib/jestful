package org.qfox.jestful.server;

import org.qfox.jestful.core.BeanContainer;
import org.qfox.jestful.core.Initialable;
import org.qfox.jestful.core.converter.ValueConverter;
import org.qfox.jestful.server.converter.*;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.*;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * Description:
 * </p>
 * <p>
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 *
 * @author Payne 646742615@qq.com
 * @date 2016年4月22日 下午4:10:50
 * @since 1.0.0
 */
public class JestfulConversionProvider implements ConversionProvider, Initialable {
    private final Set<Converter> converters = new LinkedHashSet<Converter>();

    public Object convert(String name, Type type, boolean decoded, String charset, Map<String, String[]> map) throws ConversionException, UnsupportedEncodingException {
        if (type instanceof Class<?>) return convert(name, (Class<?>) type, decoded, charset, map);
        if (type instanceof ParameterizedType) return convert(name, (ParameterizedType) type, decoded, charset, map);
        throw new UnsupportedConversionException("unsupported type " + type, name, type, map, this);
    }

    public <T> T convert(String name, Class<T> clazz, boolean decoded, String charset, Map<String, String[]> map) throws ConversionException, UnsupportedEncodingException {
        for (Converter converter : converters) if (converter.supports(clazz)) return converter.convert(name, clazz, decoded, charset, map, this);
        throw new UnsupportedConversionException("unsupported clazz " + clazz, name, clazz, map, this);
    }

    public Object convert(String name, ParameterizedType type, boolean decoded, String charset, Map<String, String[]> map) throws ConversionException, UnsupportedEncodingException {
        for (Converter converter : converters) if (converter.supports(type)) return converter.convert(name, type, decoded, charset, map, this);
        throw new UnsupportedConversionException("unsupported parameterized type " + type, name, type, map, this);
    }

    public Object getExtendConversionResult(String name, Class<?> clazz, boolean decoded, String charset, Map<String, String[]> map) throws ConversionException, UnsupportedEncodingException {
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if (!method.isAnnotationPresent(ValueConverter.class)) continue;
            if (!Modifier.isStatic(method.getModifiers())) continue;
            if (method.getReturnType() != clazz) continue;
            if (method.getGenericParameterTypes().length != 1) continue;
            Type type = method.getGenericParameterTypes()[0];
            Object value = this.convert(name, type, decoded, charset, map);
            try {
                return method.invoke(null, value);
            } catch (Exception e) {
                throw new IncompatibleConversionException(e, name, clazz, map, this);
            }
        }

        Constructor<?>[] constructors = clazz.getConstructors();
        for (Constructor<?> constructor : constructors) {
            if (!constructor.isAnnotationPresent(ValueConverter.class)) continue;
            if (constructor.getParameterTypes().length != 1) continue;
            Type type = constructor.getGenericParameterTypes()[0];
            Object value = this.convert(name, type, decoded, charset, map);
            try {
                return constructor.newInstance(value);
            } catch (Exception e) {
                throw new IncompatibleConversionException(e, name, clazz, map, this);
            }
        }

        return null;
    }

    public void initialize(BeanContainer beanContainer) {
        Map<String, ?> beans = beanContainer.find(Converter.class);
        for (Object bean : beans.values()) converters.add((Converter) bean);
    }

}
