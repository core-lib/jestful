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
public class StringBufferStringConverter implements StringConverter<StringBuffer> {

    @Override
    public boolean support(Class<?> klass) {
        return StringBuffer.class.isAssignableFrom(klass);
    }

    @Override
    public String convert(Class<?> klass, StringBuffer source) {
        return source.toString();
    }

    @Override
    public StringBuffer convert(Class<?> klass, String source) {
        try {
            return (StringBuffer) klass.getConstructor(String.class).newInstance(source);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
