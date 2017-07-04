package org.qfox.jestful.core.converter;

import java.lang.reflect.Method;

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
 * @date 2016年4月8日 上午10:58:17
 * @since 1.0.0
 */
public class EnumStringConverter implements StringConverter<Enum<?>> {

    public boolean support(Class<?> klass) {
        return Enum.class.isAssignableFrom(klass);
    }

    public String convert(Class<?> klass, Enum<?> source) {
        try {
            Method method = ValueConversion.getSerializeMethod(klass);
            if (method != null) return (String) method.invoke(source);
            return source.name();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Enum<?> convert(Class<?> klass, String source) {
        try {
            Method method = ValueConversion.getDeserializeMethod(klass);
            if (method != null) return (Enum<?>) method.invoke(null, source);
            return (Enum<?>) klass.getMethod("valueOf", String.class).invoke(null, source);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
