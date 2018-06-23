package org.qfox.jestful.core;

import org.qfox.jestful.commons.conversion.*;

import java.text.DateFormat;
import java.util.Map;

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
 * @date 2016年4月22日 下午4:10:50
 * @since 1.0.0
 */
public class JestfulConversionProvider implements ConversionProvider, Initialable {
    private ConversionProvider delegate;

    @Override
    public DateFormat getSerializationDateFormat() {
        return delegate.getSerializationDateFormat();
    }

    @Override
    public void setSerializationDateFormat(DateFormat dateFormat) {
        delegate.setSerializationDateFormat(dateFormat);
    }

    @Override
    public DateFormat getDeserializationDateFormat() {
        return delegate.getDeserializationDateFormat();
    }

    @Override
    public void setDeserializationDateFormat(DateFormat dateFormat) {
        delegate.setDeserializationDateFormat(dateFormat);
    }

    @Override
    public boolean supports(Class<?> type) {
        return delegate.supports(type);
    }

    @Override
    public Map<String, String[]> convert(String name, Object value) throws ConvertingException {
        return delegate.convert(name, value);
    }

    @Override
    public boolean supports(Conversion conversion) {
        return delegate.supports(conversion);
    }

    @Override
    public Object convert(Conversion conversion) throws ConvertingException {
        return delegate.convert(conversion);
    }

    public void initialize(BeanContainer beanContainer) {
        Map<String, Converter> beans = beanContainer.find(Converter.class);
        this.delegate = new StandardConversionProvider(beans.values());
    }

}
