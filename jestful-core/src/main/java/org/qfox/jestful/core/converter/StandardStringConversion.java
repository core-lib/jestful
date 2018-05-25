package org.qfox.jestful.core.converter;

import org.qfox.jestful.core.BeanContainer;
import org.qfox.jestful.core.Initialable;
import org.qfox.jestful.core.Parameter;
import org.qfox.jestful.core.exception.JestfulRuntimeException;
import org.qfox.jestful.core.exception.NoSuchConverterException;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

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
 * @date 2016年5月5日 上午10:53:58
 * @since 1.0.0
 */
public class StandardStringConversion implements StringConversion, Initialable {
    private final Set<StringConverter<Object>> converters = new LinkedHashSet<StringConverter<Object>>();

    @SuppressWarnings("unchecked")
    public void initialize(BeanContainer beanContainer) {
        Map<String, ?> beans = beanContainer.find(StringConverter.class);
        for (Object bean : beans.values()) {
            converters.add((StringConverter<Object>) bean);
        }
    }

    @Override
    public boolean supports(Parameter parameter) {
        return supports(parameter.getType());
    }

    private boolean supports(Type type) {
        if (type == null) return false;
        else if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type rawType = parameterizedType.getRawType();
            if (!(rawType instanceof Class<?>)) return false;
            else if (ValueConversion.getSerializeMethod((Class<?>) rawType) != null) return true;
            else if (Collection.class.isAssignableFrom((Class<?>) rawType)) return supports(parameterizedType.getActualTypeArguments()[0]);
            else if (Map.class.isAssignableFrom((Class<?>) rawType)) return supports(parameterizedType.getActualTypeArguments()[1]);
            return false;
        } else if (type instanceof Class<?>) {
            Class<?> clazz = (Class<?>) type;
            if (clazz.isArray()) return supports(clazz.getComponentType());
            else if (ValueConversion.getSerializeMethod(clazz) != null) return true;
            for (StringConverter<?> converter : converters) if (converter.support(clazz)) return true;
            return false;
        } else return false;
    }

    @Override
    public void convert(Parameter parameter, Map<String, List<String>> map) throws NoSuchConverterException {

    }

    public Map<String, List<String>> convert(Parameter parameter) throws NoSuchConverterException {
        try {
            String name = parameter.getName();
            Object value = parameter.getValue();
            return convert(name, value);
        } catch (NoSuchConverterException e) {
            throw new NoSuchConverterException(parameter);
        }
    }

    private Map<String, List<String>> convert(String name, Object value) throws NoSuchConverterException {
        name = name != null ? name.trim() : "";
        if (value == null) {
            return Collections.emptyMap();
        } else if (value instanceof Collection<?>) {
            Map<String, List<String>> map = new LinkedHashMap<String, List<String>>();
            Collection<?> collection = (Collection<?>) value;
            int index = 0;
            for (Object element : collection) map.putAll(convert(name + "[" + (index++) + "]", element));
            return map;
        } else if (value.getClass().isArray()) {
            Map<String, List<String>> map = new LinkedHashMap<String, List<String>>();
            int length = Array.getLength(value);
            for (int i = 0; i < length; i++) map.putAll(convert(name + "[" + i + "]", Array.get(value, i)));
            return map;
        } else if (value instanceof Map<?, ?>) {
            Map<String, List<String>> map = new LinkedHashMap<String, List<String>>();
            Map<?, ?> m = (Map<?, ?>) value;
            for (Map.Entry<?, ?> entry : m.entrySet()) {
                String key = name + (name.isEmpty() || entry.getKey().toString().isEmpty() ? "" : ".") + entry.getKey();
                map.putAll(convert(key, entry.getValue()));
            }
            return map;
        }

        for (StringConverter<Object> converter : converters) {
            if (converter.support(value.getClass())) {
                List<String> values = Collections.singletonList(converter.convert(value.getClass(), value));
                return Collections.singletonMap(name, values);
            }
        }

        Method method = ValueConversion.getSerializeMethod(value.getClass());
        if (method != null) {
            try {
                Map<String, List<String>> map = new LinkedHashMap<String, List<String>>();
                Map<?, ?> m = (Map<?, ?>) method.invoke(value);
                for (Map.Entry<?, ?> entry : m.entrySet()) {
                    String key = name + (name.isEmpty() || entry.getKey().toString().isEmpty() ? "" : ".") + entry.getKey();
                    List<?> list = (List<?>) entry.getValue();
                    List<String> values = new ArrayList<String>(list.size());
                    for (Object element : list) values.add((String) element);
                    map.put(key, values);
                }
                return map;
            } catch (Exception e) {
                throw new JestfulRuntimeException(e);
            }
        }

        throw new NoSuchConverterException(null);
    }

}
