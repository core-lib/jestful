package org.qfox.jestful.commons;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 方法工具
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-05-28 13:57
 **/
public class MethodKit {

    public static List<String> parameters(Method method) throws IOException {
        Methody methody = read(method);
        if (methody == null) return null;
        List<String> variables = methody.getVariables();
        return variables != null ? variables.subList(0, method.getParameterTypes().length) : null;
    }

    public static Methody read(Method method) throws IOException {
        Class<?> clazz = method.getDeclaringClass();
        Classy classy = ClassKit.read(clazz);
        if (classy == null) return null;
        String signature = sign(method);
        return classy.getMethods().get(signature);
    }

    public static String sign(Method method) {
        StringBuilder builder = new StringBuilder();
        builder.append(method.getName()).append(',');
        builder.append('(');
        for (Class<?> clazz : method.getParameterTypes()) builder.append(ClassKit.sign(clazz));
        builder.append(')');
        builder.append(ClassKit.sign(method.getReturnType()));
        return builder.toString();
    }

}
