package org.qfox.jestful.cache.generation;

import org.qfox.jestful.cache.*;
import org.qfox.jestful.cache.exception.IllegalTypeException;
import org.qfox.jestful.cache.exception.RefreshRequiredException;
import org.qfox.jestful.core.BeanContainer;
import org.qfox.jestful.core.Initialable;

import java.lang.reflect.Method;

/**
 * Created by yangchangpei on 17/9/6.
 */
public class SimpleGenerator implements Generator, Initialable {

    private Conversion conversion;

    @Override
    public String generate(Object object, Method method, Parameter... parameters) throws IllegalTypeException, RefreshRequiredException {
        StringBuilder key = new StringBuilder();
        addObjectCacheKey(object, method, key);
        addMethodCacheKey(method, key);
        addParamsCacheKey(parameters, key);
        return key.toString();
    }

    protected void addParamsCacheKey(Parameter[] parameters, StringBuilder key) {
        key.append("(");
        boolean first = true;
        for (Parameter parameter : parameters) {
            if (parameter.isIgnored() || parameter.isRefresh() || parameter.isDuration()) continue;
            if (first) {
                first = false;
                key.append(parameter.name());
                key.append("=");
                key.append(parameter.convert(conversion));
            } else {
                key.append(",");
                key.append(parameter.name());
                key.append("=");
                key.append(parameter.convert(conversion));
            }
        }
        key.append(")");
    }

    protected void addMethodCacheKey(Method method, StringBuilder key) {
        // 方法前面加 #
        key.append("#");
        // 重写方法名
        if (method.isAnnotationPresent(Cacheable.class) && method.getAnnotation(Cacheable.class).value().length() > 0) {
            key.append(method.getAnnotation(Cacheable.class).value());
        }
        // 默认方法名
        else {
            key.append(method.getName());
        }
    }

    protected void addObjectCacheKey(Object object, Method method, StringBuilder key) {
        // 静态方法
        if (object == null) {
            key.append(method.getDeclaringClass().getName());
        }
        // 实现了Caching接口
        else if (object instanceof Caching) {
            key.append(Caching.class.cast(object).toCacheKey());
        }
        // 标注了 @Cacheable 注解
        else if (object.getClass().isAnnotationPresent(Cacheable.class) && object.getClass().getAnnotation(Cacheable.class).value().length() > 0) {
            key.append(object.getClass().getAnnotation(Cacheable.class).value());
        }
        // 默认用 类名 + @ + hash code
        else {
            key.append(object.getClass().getName()).append("@").append(Integer.toHexString(object.hashCode()));
        }
    }

    @Override
    public void initialize(BeanContainer beanContainer) {
        conversion = beanContainer.get(Conversion.class);
    }
}
