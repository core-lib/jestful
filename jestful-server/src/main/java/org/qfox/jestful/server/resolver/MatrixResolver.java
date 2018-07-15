package org.qfox.jestful.server.resolver;

import org.qfox.jestful.commons.StringKit;
import org.qfox.jestful.commons.conversion.Conversion;
import org.qfox.jestful.commons.conversion.ConversionProvider;
import org.qfox.jestful.core.*;

import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MatrixResolver implements Resolver, Initialable {
    private ConversionProvider conversionProvider;

    @Override
    public boolean supports(Action action, Parameter parameter) {
        return parameter.between(Position.MATRIX) && parameter.getValue() == null;
    }

    @Override
    public void resolve(Action action, Parameter parameter) throws Exception {
        String URI = action.getServletURI();
        Pattern pattern = action.getPattern();
        String regex = pattern.pattern();
        if (!URI.endsWith("/") && regex.endsWith("/")) URI = URI.concat("/");
        String charset = action.getPathEncodeCharset();
        Matcher matcher = pattern.matcher(URI);
        if (!matcher.find()) throw new IllegalStateException("uri " + URI + " is not match with " + pattern);
        boolean decoded = !parameter.isCoding() || (parameter.isCoding() && parameter.isDecoded());
        String pathname = parameter.property("path");
        int count = matcher.groupCount();
        String name = parameter.getName();
        Type type = parameter.getType();
        if (StringKit.isEmpty(pathname)) {
            for (int index = 1; index <= count; index++) {
                String group = matcher.group(index);
                String[] items = group.split("\\s*;\\s*");
                for (int i = 1; i < items.length; i++) {
                    String item = items[i];
                    int idx = item.indexOf('=');
                    String key = idx < 0 ? item : item.substring(0, idx);
                    String val = idx < 0 ? "" : item.substring(idx + 1, item.length());
                    String[] values = val.split("\\s*,\\s*");
                    Conversion conversion = new Conversion(name, parameter.getValue(), type, decoded, charset, key, values);
                    Object value = conversionProvider.convert(conversion);
                    parameter.setValue(value);
                }
            }
        } else {
            Map<String, Integer> map = new LinkedHashMap<String, Integer>();
            List<Parameter> paths = action.getParameters().all(Position.PATH);
            for (Parameter path : paths) map.put(path.getName(), path.getGroup());
            Integer index = map.get(pathname);
            if (index == null) return;
            String group = matcher.group(index);
            String[] items = group.split("\\s*;\\s*");
            for (int i = 1; i < items.length; i++) {
                String item = items[i];
                int idx = item.indexOf('=');
                String key = idx < 0 ? item : item.substring(0, idx);
                String val = idx < 0 ? "" : item.substring(idx + 1, item.length());
                String[] values = val.split("\\s*,\\s*");
                Conversion conversion = new Conversion(name, parameter.getValue(), type, decoded, charset, key, values);
                Object value = conversionProvider.convert(conversion);
                parameter.setValue(value);
            }
        }
    }

    public void initialize(BeanContainer beanContainer) {
        this.conversionProvider = beanContainer.get(ConversionProvider.class);
    }
}
