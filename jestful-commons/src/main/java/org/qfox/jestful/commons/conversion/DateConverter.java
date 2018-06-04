package org.qfox.jestful.commons.conversion;

import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 日期转换器
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-06-04 10:58
 **/
public class DateConverter implements Converter<Date> {
    private final Map<Pattern, DateFormat> formats = new LinkedHashMap<Pattern, DateFormat>();

    {
        formats.put(Pattern.compile("\\d{1,2}\\/\\d{1,2}\\/\\d{4,}"), new SimpleDateFormat("MM/dd/yyyy"));
        formats.put(Pattern.compile("\\d{4,}\\-\\d{1,2}\\-\\d{1,2}"), new SimpleDateFormat("yyyy-MM-dd"));
        formats.put(Pattern.compile("\\d{4,}\\/\\d{1,2}\\/\\d{1,2}"), new SimpleDateFormat("yyyy/MM/dd"));
        formats.put(Pattern.compile("\\d{4,}\\s\\d{1,2}\\s\\d{1,2}"), new SimpleDateFormat("yyyy MM dd"));
        formats.put(Pattern.compile("\\d{4,}\\.\\d{1,2}\\.\\d{1,2}"), new SimpleDateFormat("yyyy.MM.dd"));
        formats.put(Pattern.compile("\\d{1,2}\\/\\d{1,2}\\/\\d{4,}\\s\\d{1,2}\\:\\d{1,2}\\:\\d{1,2}"), new SimpleDateFormat("MM/dd/yyyy HH:mm:ss"));
        formats.put(Pattern.compile("\\d{4,}\\-\\d{1,2}\\-\\d{1,2}\\s\\d{1,2}\\:\\d{1,2}\\:\\d{1,2}"), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        formats.put(Pattern.compile("\\d{4,}\\/\\d{1,2}\\/\\d{1,2}\\s\\d{1,2}\\:\\d{1,2}\\:\\d{1,2}"), new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"));
        formats.put(Pattern.compile("\\d{4,}\\s\\d{1,2}\\s\\d{1,2}\\s\\d{1,2}\\:\\d{1,2}\\:\\d{1,2}"), new SimpleDateFormat("yyyy MM dd HH:mm:ss"));
        formats.put(Pattern.compile("\\d{4,}\\.\\d{1,2}\\.\\d{1,2}\\s\\d{1,2}\\:\\d{1,2}\\:\\d{1,2}"), new SimpleDateFormat("yyyy.MM.dd HH:mm:ss"));
    }

    @Override
    public boolean supports(Conversion conversion) {
        return conversion.type == Date.class;
    }

    @Override
    public Date convert(Conversion conversion, ConversionProvider provider) throws Exception {
        if (!supports(conversion) || !conversion.name.equals(conversion.expression)) return (Date) conversion.value;
        String[] values = conversion.values;
        boolean decoded = conversion.decoded;
        String charset = conversion.charset;
        String value = values != null && values.length > 0 ? values[0] : null;
        if (value == null) return null;
        if (!decoded) value = URLDecoder.decode(value, charset);
        if (value.matches("\\d+")) return new Date(Long.valueOf(value));
        for (Map.Entry<Pattern, DateFormat> entry : formats.entrySet()) if (entry.getKey().matcher(value).matches()) return entry.getValue().parse(value);
        return (Date) conversion.value;
    }
}
