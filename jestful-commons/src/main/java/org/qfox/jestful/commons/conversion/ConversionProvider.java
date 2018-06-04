package org.qfox.jestful.commons.conversion;

/**
 * 转换提供器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-06-04 10:10
 **/
public interface ConversionProvider {

    /**
     * 黑盒转换, 屏蔽所有细节,提供所有类型的转换
     *
     * @param conversion 转换动作
     * @return 转换后的结果
     * @throws ConvertingException 转换异常
     */
    Object convert(Conversion conversion) throws ConvertingException;

}
