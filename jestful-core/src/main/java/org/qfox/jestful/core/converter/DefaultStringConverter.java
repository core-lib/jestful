package org.qfox.jestful.core.converter;

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
 * @date 2016年4月8日 上午10:57:09
 * @since 1.0.0
 */
public class DefaultStringConverter implements StringConverter<String> {

    public boolean support(Class<?> klass) {
        return klass == String.class;
    }

    public String convert(Class<?> klass, String source) {
        return source;
    }

}
