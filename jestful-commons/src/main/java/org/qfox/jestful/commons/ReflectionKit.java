package org.qfox.jestful.commons;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 反射工具
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-05-15 11:00
 **/
public class ReflectionKit {

    public static boolean isListType(Type type) {
        return type == List.class
                || (type instanceof ParameterizedType && ((ParameterizedType) type).getRawType() == List.class);
    }

    public static boolean isListType(Type type, Class<?> actualTypeArgument) {
        return type instanceof ParameterizedType
                && isListType(((ParameterizedType) type).getRawType())
                && ((ParameterizedType) type).getActualTypeArguments()[0] instanceof Class<?>
                && ((Class<?>) ((ParameterizedType) type).getActualTypeArguments()[0]).isAssignableFrom(actualTypeArgument);
    }

    public static boolean isSetType(Type type) {
        return type == Set.class
                || (type instanceof ParameterizedType && ((ParameterizedType) type).getRawType() == Set.class);
    }

    public static boolean isSetType(Type type, Class<?> actualTypeArgument) {
        return type instanceof ParameterizedType
                && isSetType(((ParameterizedType) type).getRawType())
                && ((ParameterizedType) type).getActualTypeArguments()[0] instanceof Class<?>
                && ((Class<?>) ((ParameterizedType) type).getActualTypeArguments()[0]).isAssignableFrom(actualTypeArgument);
    }

    public static boolean isCollectionType(Type type) {
        return type == Collection.class
                || (type instanceof ParameterizedType && ((ParameterizedType) type).getRawType() == Collection.class);
    }

    public static boolean isCollectionType(Type type, Class<?> actualTypeArgument) {
        return type instanceof ParameterizedType
                && isCollectionType(((ParameterizedType) type).getRawType())
                && ((ParameterizedType) type).getActualTypeArguments()[0] instanceof Class<?>
                && ((Class<?>) ((ParameterizedType) type).getActualTypeArguments()[0]).isAssignableFrom(actualTypeArgument);
    }

}
