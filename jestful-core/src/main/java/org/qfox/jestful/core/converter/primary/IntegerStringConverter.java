package org.qfox.jestful.core.converter.primary;

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
public class IntegerStringConverter implements StringConverter<Integer> {

    public boolean support(Class<?> klass) {
        return klass == int.class;
    }

    public String convert(Class<?> klass, Integer source) {
        return String.valueOf(source);
    }

    public Integer convert(Class<?> klass, String source) {
        return source != null && source.length() == 0 == false ? Integer.valueOf(source) : 0;
    }

}
