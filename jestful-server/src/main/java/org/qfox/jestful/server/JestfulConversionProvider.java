package org.qfox.jestful.server;

import org.qfox.jestful.commons.conversion.*;
import org.qfox.jestful.core.BeanContainer;
import org.qfox.jestful.core.Initialable;

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
    public Object convert(Conversion conversion) throws ConvertingException {
        return delegate.convert(conversion);
    }

    public void initialize(BeanContainer beanContainer) {
        Map<String, Converter> beans = beanContainer.find(Converter.class);
        this.delegate = new StandardConversionProvider(beans.values());
    }

}
