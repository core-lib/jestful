package org.qfox.jestful.commons;

import org.qfox.jestful.commons.type.GenericArrayTypeImpl;
import org.qfox.jestful.commons.type.ParameterizedTypeImpl;

import java.lang.reflect.*;
import java.util.*;

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

    public static Type derive(final Class<?> root, final Type type) {
        if (type instanceof TypeVariable<?>) {
            TypeVariable<?> typeVariable = (TypeVariable<?>) type;
            GenericDeclaration genericDeclaration = typeVariable.getGenericDeclaration();
            TypeVariable<?>[] typeParameters = genericDeclaration.getTypeParameters();
            int index;
            for (index = 0; index < typeParameters.length; index++) {
                if (typeParameters[index].getName().equals(typeVariable.getName())) {
                    break;
                }
            }
            return ReflectionKit.derive(root, (Class<?>) genericDeclaration, index);
        } else if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            Type[] derivedActualTypeArguments = new Type[actualTypeArguments.length];
            for (int i = 0; i < actualTypeArguments.length; i++) {
                derivedActualTypeArguments[i] = derive(root, actualTypeArguments[i]);
            }
            Type rawType = parameterizedType.getRawType();
            Type ownerType = parameterizedType.getOwnerType();
            return new ParameterizedTypeImpl(rawType, ownerType, derivedActualTypeArguments);
        } else if (type instanceof GenericArrayType) {
            GenericArrayType genericArrayType = (GenericArrayType) type;
            Type genericComponentType = genericArrayType.getGenericComponentType();
            Type derivedGenericComponentType = derive(root, genericComponentType);
            return new GenericArrayTypeImpl(derivedGenericComponentType);
        } else if (type instanceof WildcardType) {
            WildcardType wildcardType = (WildcardType) type;
            if (wildcardType.getLowerBounds() != null && wildcardType.getLowerBounds().length > 0) {
                return derive(root, wildcardType.getLowerBounds()[0]);
            }
            if (wildcardType.getUpperBounds() != null && wildcardType.getUpperBounds().length > 0) {
                return derive(root, wildcardType.getUpperBounds()[0]);
            }
            throw new IllegalStateException();
        } else {
            return type;
        }
    }

    private static Type derive(final Class<?> root, final Class<?> type, final int index) {
        TypeVariable<? extends Class<?>>[] typeParameters = type.getTypeParameters();
        if (index < 0 || typeParameters.length <= index) {
            throw new IndexOutOfBoundsException("type parameters index range of " + type + " is [0, " + typeParameters.length + ") which do not contains " + index);
        }
        return derive(root, type, index, Collections.<String, Type>emptyMap());
    }

    private static Type derive(final Class<?> root, final Class<?> type, final int index, final Map<String, Type> typeArguments) {
        Type[] interfaces = root.getGenericInterfaces();
        // 先从接口开始寻找
        for (Type interfase : interfaces) {
            // 父类是一个泛型类
            if (interfase instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) interfase;
                Type derived = derive(parameterizedType, type, index, typeArguments);
                if (derived != null) {
                    return derived;
                }
            }
            // 父类是一个普通类，之前的推导类型参数映射在这里断了！
            if (interfase instanceof Class<?>) {
                Class<?> clazz = (Class<?>) interfase;
                Type derived = derive(clazz, type, index, Collections.<String, Type>emptyMap());
                if (derived != null) {
                    return derived;
                }
            }
        }
        // 再从父类开始寻找
        Type superclass = root.getGenericSuperclass();
        // 父类是一个泛型类
        if (superclass instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) superclass;
            return derive(parameterizedType, type, index, typeArguments);
        }
        // 父类是一个普通类，之前的推导类型参数映射在这里断了！
        if (superclass instanceof Class<?>) {
            Class<?> clazz = (Class<?>) superclass;
            return derive(clazz, type, index, Collections.<String, Type>emptyMap());
        }
        // 如果没有匹配的则返回null
        return null;
    }

    private static Type derive(final ParameterizedType parameterizedType, final Class<?> type, final int index, final Map<String, Type> typeArguments) {
        Class<?> rawType = (Class<?>) parameterizedType.getRawType();
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        // 找到了
        if (rawType == type) {
            Type actualTypeArgument = actualTypeArguments[index];
            if (actualTypeArgument instanceof TypeVariable<?>) {
                TypeVariable<?> typeVariable = (TypeVariable<?>) actualTypeArgument;
                String name = typeVariable.getName();
                actualTypeArgument = typeArguments.containsKey(name) ? typeArguments.get(name) : actualTypeArgument;
            }
            return actualTypeArgument;
        }
        // 继续推导，而且传递祖先类中被推倒出的类型参数实际类型。
        else {
            TypeVariable<? extends Class<?>>[] typeParameters = rawType.getTypeParameters();
            Map<String, Type> derivedArguments = new HashMap<String, Type>();
            for (int i = 0; i < typeParameters.length && i < actualTypeArguments.length; i++) {
                TypeVariable<? extends Class<?>> typeParameter = typeParameters[i];
                Type actualTypeArgument = actualTypeArguments[i];
                if (actualTypeArgument instanceof TypeVariable<?>) {
                    TypeVariable<?> typeVariable = (TypeVariable<?>) actualTypeArgument;
                    String name = typeVariable.getName();
                    actualTypeArgument = typeArguments.containsKey(name) ? typeArguments.get(name) : actualTypeArgument;
                }
                String name = typeParameter.getName();
                derivedArguments.put(name, actualTypeArgument);
            }
            return derive(rawType, type, index, derivedArguments);
        }
    }
}
