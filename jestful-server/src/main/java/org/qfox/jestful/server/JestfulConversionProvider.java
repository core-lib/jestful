package org.qfox.jestful.server;

import org.qfox.jestful.core.BeanContainer;
import org.qfox.jestful.core.Initialable;
import org.qfox.jestful.server.converter.ConversionException;
import org.qfox.jestful.server.converter.ConversionProvider;
import org.qfox.jestful.server.converter.Converter;
import org.qfox.jestful.server.converter.UnsupportedConversionException;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
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

    public void initialize(BeanContainer beanContainer) {
        Map<String, ?> beans = beanContainer.find(Converter.class);
        for (Object bean : beans.values()) converters.add((Converter) bean);
    }

}
