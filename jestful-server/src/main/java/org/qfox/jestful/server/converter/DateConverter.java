package org.qfox.jestful.server.converter;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.ParameterizedType;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

public class DateConverter implements Converter {
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

    public boolean supports(Class<?> clazz) {
        return Date.class == clazz;
    }

    public <T> T convert(String name, Class<T> clazz, boolean decoded, String charset, Map<String, String[]> map, ConversionProvider provider) throws ConversionException, UnsupportedEncodingException {
        String[] values = map.get(name) != null ? map.get(name).clone() : null;
        String value = values != null && values.length > 0 ? values[0] : null;
        if (value == null) {
            return null;
        }
        if (decoded == false) {
            value = URLDecoder.decode(value, charset);
        }
        if (value.matches("\\d+")) {
            return clazz.cast(new Date(Long.valueOf(value)));
        }
        for (Entry<Pattern, DateFormat> entry : formats.entrySet()) {
            if (entry.getKey().matcher(value).matches()) {
                try {
                    return clazz.cast(entry.getValue().parse(value));
                } catch (ParseException e) {
                    throw new IncompatibleConversionException(e, name, clazz, map, provider);
                }
            }
        }
        throw new IncompatibleConversionException("unsupported date format of " + value + " please use one of " + formats.keySet(), name, clazz, map, provider);
    }

    public boolean supports(ParameterizedType type) {
        return false;
    }

    public Object convert(String name, ParameterizedType type, boolean decoded, String charset, Map<String, String[]> map, ConversionProvider provider) throws ConversionException, UnsupportedEncodingException {
        throw new UnsupportedOperationException("converter of " + this.getClass() + " do not supported parameterized type");
    }

}
