package org.qfox.jestful.core.converter;

import org.qfox.jestful.core.BeanContainer;
import org.qfox.jestful.core.Initialable;
import org.qfox.jestful.core.Parameter;
import org.qfox.jestful.core.exception.NoSuchConverterException;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
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
 * @date 2016年5月5日 上午10:53:58
 * @since 1.0.0
 */
public class DefaultStringConversion implements StringConversion, Initialable {
    private final Set<StringConverter<Object>> converters = new LinkedHashSet<StringConverter<Object>>();

    @SuppressWarnings("unchecked")
    public void initialize(BeanContainer beanContainer) {
        Map<String, ?> beans = beanContainer.find(StringConverter.class);
        for (Object bean : beans.values()) {
            converters.add((StringConverter<Object>) bean);
        }
    }

    public boolean serializable(Parameter parameter) {
        Class<?> klass = parameter.getKlass();
        // 兼容一维数组
        if (klass.isArray()) {
            klass = klass.getComponentType();
        }
        // 支持简单类型
        for (StringConverter<?> converter : converters) {
            if (converter.support(klass)) {
                return true;
            }
        }
        // 支持复合类型 当他有对应的转换器方法
        return getSerializeMethod(klass) != null;
    }

    protected Method getSerializeMethod(Class<?> klass) {
        // 1. 有标注有ValueConverter注解的, 公开的, 静态的, 返回值类型为自身的, 只接收一个参数而且为String类型的方法
        Method[] methods = klass.getMethods();
        for (Method method : methods) {
            if (!method.isAnnotationPresent(ValueConverter.class)) continue;
            if (Modifier.isStatic(method.getModifiers())) continue;
            if (method.getReturnType() != String.class) continue;
            if (method.getParameterTypes().length > 0) continue;
            return method;
        }
        return null;
    }

    public boolean deserializable(Parameter parameter) {
        Class<?> klass = parameter.getKlass();
        // 兼容一维数组
        if (klass.isArray()) {
            klass = klass.getComponentType();
        }
        // 支持简单类型
        for (StringConverter<?> converter : converters) {
            if (converter.support(klass)) {
                return true;
            }
        }
        // 支持复合类型 当他有对应的转换器方法
        return getDeserializeMethod(klass) != null || getDeserializeConstructor(klass) != null;
    }

    protected Method getDeserializeMethod(Class<?> klass) {
        // 1. 有标注有ValueConverter注解的, 公开的, 静态的, 返回值类型为自身的, 只接收一个参数而且为String类型的方法
        Method[] methods = klass.getMethods();
        for (Method method : methods) {
            if (!method.isAnnotationPresent(ValueConverter.class)) continue;
            if (!Modifier.isStatic(method.getModifiers())) continue;
            if (method.getReturnType() != klass) continue;
            if (method.getParameterTypes().length != 1 || method.getParameterTypes()[0] != String.class) continue;
            return method;
        }
        return null;
    }

    protected Constructor<?> getDeserializeConstructor(Class<?> klass) {
        // 2. 有标注有ValueConverter注解的, 公开的, 只接收一个参数而且为String类型的构造方法
        Constructor<?>[] constructors = klass.getConstructors();
        for (Constructor<?> constructor : constructors) {
            if (!constructor.isAnnotationPresent(ValueConverter.class)) continue;
            if (constructor.getParameterTypes().length != 1 || constructor.getParameterTypes()[0] != String.class) continue;
            return constructor;
        }
        return null;
    }

    public void convert(Parameter parameter, String source) throws NoSuchConverterException {
        Class<?> klass = parameter.getKlass();
        if (klass.isArray()) {
            // 匹配基本转换
            klass = klass.getComponentType();
            for (StringConverter<?> converter : converters) {
                if (converter.support(klass)) {
                    Object value = converter.convert(klass, source);
                    push(parameter, klass, value);
                    return;
                }
            }

            // 匹配转换方法
            Method method = getDeserializeMethod(klass);
            if (method != null) {
                try {
                    Object value = method.invoke(null, source);
                    push(parameter, klass, value);
                    return;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            // 匹配转换构造器
            Constructor<?> constructor = getDeserializeConstructor(klass);
            if (constructor != null) {
                try {
                    Object value = constructor.newInstance(source);
                    push(parameter, klass, value);
                    return;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            throw new NoSuchConverterException(parameter);
        } else {
            for (StringConverter<?> converter : converters) {
                if (converter.support(klass)) {
                    Object value = converter.convert(klass, source);
                    parameter.setValue(value);
                    return;
                }
            }

            // 匹配转换方法
            Method method = getDeserializeMethod(klass);
            if (method != null) {
                try {
                    Object value = method.invoke(null, source);
                    parameter.setValue(value);
                    return;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            // 匹配转换构造器
            Constructor<?> constructor = getDeserializeConstructor(klass);
            if (constructor != null) {
                try {
                    Object value = constructor.newInstance(source);
                    parameter.setValue(value);
                    return;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            throw new NoSuchConverterException(parameter);
        }
    }

    protected void push(Parameter parameter, Class<?> klass, Object value) {
        Object array = parameter.getValue();
        if (array == null) {
            array = Array.newInstance(klass, 1);
            Array.set(array, 0, value);
            parameter.setValue(array);
        } else if (array.getClass().isArray()) {
            int length = Array.getLength(array);
            Object _array = Array.newInstance(klass, length + 1);
            System.arraycopy(array, 0, _array, 0, length);
            Array.set(_array, length, value);
            parameter.setValue(_array);
        } else {
            throw new IllegalStateException("expecting parameter value is type of array but got " + array.getClass());
        }
    }

    public String[] convert(Parameter parameter) throws NoSuchConverterException {
        Class<?> klass = parameter.getKlass();
        if (klass.isArray()) {
            klass = klass.getComponentType();
            Object array = parameter.getValue();
            if (array == null) {
                return null;
            } else if (array.getClass().isArray()) {
                StringConverter<Object> converter = null;
                for (StringConverter<Object> c : converters) {
                    if (c.support(klass)) {
                        converter = c;
                        break;
                    }
                }
                if (converter != null) {
                    int length = Array.getLength(array);
                    String[] targets = new String[length];
                    for (int index = 0; index < length; index++) {
                        Object element = Array.get(array, index);
                        String target = converter.convert(klass, element);
                        targets[index] = target;
                    }
                    return targets;
                }

                // 匹配转换方法
                Method method = getSerializeMethod(klass);
                if (method != null) {
                    try {
                        int length = Array.getLength(array);
                        String[] targets = new String[length];
                        for (int index = 0; index < length; index++) {
                            Object element = Array.get(array, index);
                            String target = (String) method.invoke(element);
                            targets[index] = target;
                        }
                        return targets;
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }

                throw new NoSuchConverterException(parameter);
            } else {
                throw new IllegalStateException("expecting parameter value is type of array but got " + array.getClass());
            }
        } else {
            for (StringConverter<Object> converter : converters) {
                if (converter.support(klass)) {
                    Object source = parameter.getValue();
                    String target = converter.convert(klass, source);
                    return new String[]{target};
                }
            }

            // 匹配转换方法
            Method method = getSerializeMethod(klass);
            if (method != null) {
                try {
                    Object source = parameter.getValue();
                    String target = (String) method.invoke(source);
                    return new String[]{target};
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            throw new NoSuchConverterException(parameter);
        }
    }
}
