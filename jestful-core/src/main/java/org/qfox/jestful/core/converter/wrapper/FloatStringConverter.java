package org.qfox.jestful.core.converter.wrapper;

import org.qfox.jestful.core.converter.StringConverter;

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
 * @date 2016年4月8日 上午10:36:12
 * @since 1.0.0
 */
public class FloatStringConverter implements StringConverter<Float> {

    public boolean support(Class<?> klass) {
        return klass == Float.class;
    }

    public String convert(Class<?> klass, Float source) {
        return source == null ? null : String.valueOf(source);
    }

    public Float convert(Class<?> klass, String source) {
        return source != null && source.length() == 0 == false ? Float.valueOf(source) : null;
    }

}
