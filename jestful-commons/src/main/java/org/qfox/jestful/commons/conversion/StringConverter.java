package org.qfox.jestful.commons.conversion;

import java.net.URLDecoder;

/**
 * 字符串类型转换器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-06-04 11:06
 **/
public class StringConverter implements Converter<CharSequence> {

    @Override
    public boolean supports(Conversion conversion) {
        return conversion.type == String.class
                || conversion.type == StringBuilder.class
                || conversion.type == StringBuffer.class
                || conversion.type == CharSequence.class;
    }

    @Override
    public CharSequence convert(Conversion conversion, ConversionProvider provider) throws Exception {
        if (!supports(conversion) || !conversion.name.equals(conversion.expression)) return (CharSequence) conversion.value;
        else if (conversion.type == String.class) return conversion.decoded ? conversion.values[0] : URLDecoder.decode(conversion.values[0], conversion.charset);
        else if (conversion.type == StringBuilder.class) return new StringBuilder(conversion.decoded ? conversion.values[0] : URLDecoder.decode(conversion.values[0], conversion.charset));
        else if (conversion.type == StringBuffer.class) return new StringBuffer(conversion.decoded ? conversion.values[0] : URLDecoder.decode(conversion.values[0], conversion.charset));
        else return (CharSequence) conversion.value;
    }

}
