package org.qfox.jestful.commons;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 反射工具
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-05-15 11:00
 **/
public class ReflectionKit {

    public static boolean isArrayType(Type type) {
        return type instanceof Class<?> && ((Class<?>) type).isArray();
    }

    public static boolean isArrayType(Type type, Class<?> itemType) {
        return isArrayType(type) && ((Class<?>) type).getComponentType().isAssignableFrom(itemType);
    }

    public static boolean isListType(Type type) {
        return type == List.class
                || (type instanceof ParameterizedType && ((ParameterizedType) type).getRawType() == List.class);
    }

    public static boolean isListType(Type type, Class<?> itemType) {
        return type instanceof ParameterizedType
                && isListType(((ParameterizedType) type).getRawType())
                && ((ParameterizedType) type).getActualTypeArguments()[0] instanceof Class<?>
                && ((Class<?>) ((ParameterizedType) type).getActualTypeArguments()[0]).isAssignableFrom(itemType);
    }

    public static boolean isSetType(Type type) {
        return type == Set.class
                || (type instanceof ParameterizedType && ((ParameterizedType) type).getRawType() == Set.class);
    }

    public static boolean isSetType(Type type, Class<?> itemType) {
        return type instanceof ParameterizedType
                && isSetType(((ParameterizedType) type).getRawType())
                && ((ParameterizedType) type).getActualTypeArguments()[0] instanceof Class<?>
                && ((Class<?>) ((ParameterizedType) type).getActualTypeArguments()[0]).isAssignableFrom(itemType);
    }

    public static boolean isCollectionType(Type type) {
        return type == Collection.class
                || (type instanceof ParameterizedType && ((ParameterizedType) type).getRawType() == Collection.class);
    }

    public static boolean isCollectionType(Type type, Class<?> itemType) {
        return type instanceof ParameterizedType
                && isCollectionType(((ParameterizedType) type).getRawType())
                && ((ParameterizedType) type).getActualTypeArguments()[0] instanceof Class<?>
                && ((Class<?>) ((ParameterizedType) type).getActualTypeArguments()[0]).isAssignableFrom(itemType);
    }

    public static boolean isMapType(Type type) {
        return type == Map.class
                || (type instanceof ParameterizedType && ((ParameterizedType) type).getRawType() == Map.class);
    }

    public static boolean isMapType(Type type, Class<?> keyType, Class<?> valueType) {
        return type instanceof ParameterizedType
                && isMapType(((ParameterizedType) type).getRawType())
                && ((ParameterizedType) type).getActualTypeArguments()[0] instanceof Class<?>
                && ((Class<?>) ((ParameterizedType) type).getActualTypeArguments()[0]).isAssignableFrom(keyType)
                && ((ParameterizedType) type).getActualTypeArguments()[1] instanceof Class<?>
                && ((Class<?>) ((ParameterizedType) type).getActualTypeArguments()[1]).isAssignableFrom(valueType);
    }

    public static boolean isMapArrayType(Type type, Class<?> keyType, Class<?> itemType) {
        return type instanceof ParameterizedType
                && isMapType(((ParameterizedType) type).getRawType())
                && ((ParameterizedType) type).getActualTypeArguments()[0] instanceof Class<?>
                && ((Class<?>) ((ParameterizedType) type).getActualTypeArguments()[0]).isAssignableFrom(keyType)
                && ((ParameterizedType) type).getActualTypeArguments()[1] instanceof Class<?>
                && isArrayType(((ParameterizedType) type).getActualTypeArguments()[1], itemType);
    }

    public static boolean isMapListType(Type type, Class<?> keyType, Class<?> itemType) {
        return type instanceof ParameterizedType
                && isMapType(((ParameterizedType) type).getRawType())
                && ((ParameterizedType) type).getActualTypeArguments()[0] instanceof Class<?>
                && ((Class<?>) ((ParameterizedType) type).getActualTypeArguments()[0]).isAssignableFrom(keyType)
                && ((ParameterizedType) type).getActualTypeArguments()[1] instanceof ParameterizedType
                && isListType(((ParameterizedType) type).getActualTypeArguments()[1], itemType);
    }

    public static boolean isMapSetType(Type type, Class<?> keyType, Class<?> itemType) {
        return type instanceof ParameterizedType
                && isMapType(((ParameterizedType) type).getRawType())
                && ((ParameterizedType) type).getActualTypeArguments()[0] instanceof Class<?>
                && ((Class<?>) ((ParameterizedType) type).getActualTypeArguments()[0]).isAssignableFrom(keyType)
                && ((ParameterizedType) type).getActualTypeArguments()[1] instanceof ParameterizedType
                && isSetType(((ParameterizedType) type).getActualTypeArguments()[1], itemType);
    }

    public static boolean isMapCollectionType(Type type, Class<?> keyType, Class<?> itemType) {
        return type instanceof ParameterizedType
                && isMapType(((ParameterizedType) type).getRawType())
                && ((ParameterizedType) type).getActualTypeArguments()[0] instanceof Class<?>
                && ((Class<?>) ((ParameterizedType) type).getActualTypeArguments()[0]).isAssignableFrom(keyType)
                && ((ParameterizedType) type).getActualTypeArguments()[1] instanceof ParameterizedType
                && isCollectionType(((ParameterizedType) type).getActualTypeArguments()[1], itemType);
    }

}
