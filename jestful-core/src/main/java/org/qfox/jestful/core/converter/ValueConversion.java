package org.qfox.jestful.core.converter;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Created by yangchangpei on 17/7/4.
 */
public class ValueConversion {

    public static Method getSerializeMethod(Class<?> klass) {
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

    public static Method getDeserializeMethod(Class<?> klass) {
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

    public static Constructor<?> getDeserializeConstructor(Class<?> klass) {
        // 2. 有标注有ValueConverter注解的, 公开的, 只接收一个参数而且为String类型的构造方法
        Constructor<?>[] constructors = klass.getConstructors();
        for (Constructor<?> constructor : constructors) {
            if (!constructor.isAnnotationPresent(ValueConverter.class)) continue;
            if (constructor.getParameterTypes().length != 1 || constructor.getParameterTypes()[0] != String.class) continue;
            return constructor;
        }
        return null;
    }

}
