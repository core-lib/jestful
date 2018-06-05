package org.qfox.jestful.server.formatting;

import org.qfox.jestful.commons.ArrayKit;
import org.qfox.jestful.commons.IOKit;
import org.qfox.jestful.commons.conversion.Conversion;
import org.qfox.jestful.commons.conversion.ConversionProvider;
import org.qfox.jestful.commons.conversion.ConvertingException;
import org.qfox.jestful.core.*;
import org.qfox.jestful.core.io.MultipartInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * 表单工具
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-05-17 15:39
 **/
public class FormKit {

    public static void assign(String charset, Map<String, String[]> map, List<Parameter> parameters, ConversionProvider conversionProvider) throws IOException {
        try {
            for (Parameter parameter : parameters) {
                String name = parameter.getName();
                Type type = parameter.getType();
                boolean decoded = !parameter.isCoding() || (parameter.isCoding() && parameter.isDecoded());
                for (Map.Entry<String, String[]> entry : map.entrySet()) {
                    String expression = entry.getKey();
                    String[] values = entry.getValue();
                    Object value = parameter.getValue();
                    Conversion conversion = new Conversion(name, value, type, decoded, charset, expression, values);
                    value = conversionProvider.convert(conversion);
                    parameter.setValue(value);
                }
            }
        } catch (ConvertingException e) {
            throw new IOException(e);
        }
    }

    public static void extract(InputStream in, String boundary, List<Multipart> multiparts, Map<String, String[]> map) throws IOException {
        MultipartInputStream mis = null;
        try {
            mis = new MultipartInputStream(in, boundary);
            Multihead multihead;
            while ((multihead = mis.getNextMultihead()) != null) {
                MediaType type = multihead.getType();
                Disposition disposition = multihead.getDisposition();
                if (type != null) {
                    Multibody multibody = new Multibody(mis);
                    Multipart multipart = new Multipart(multihead, multibody);
                    multiparts.add(multipart);
                } else if (disposition != null) {
                    String name = disposition.getName();
                    String value = IOKit.toString(mis);
                    String[] values = map.get(name);
                    map.put(name, ArrayKit.append(values != null ? values : new String[0], value));
                }
            }
        } finally {
            IOKit.close(mis);
        }
    }

}
