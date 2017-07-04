package org.qfox.jestful.core.converter;

import org.qfox.jestful.core.Parameter;
import org.qfox.jestful.core.exception.NoSuchConverterException;

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
 * @date 2016年5月5日 上午10:39:31
 * @since 1.0.0
 */
public interface StringConversion {

    boolean serializable(Parameter parameter);

    boolean deserializable(Parameter parameter);

    void convert(Parameter parameter, String source) throws NoSuchConverterException;

    String[] convert(Parameter parameter) throws NoSuchConverterException;

}
