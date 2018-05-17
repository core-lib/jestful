package org.qfox.jestful.server.formatting;

import org.qfox.jestful.commons.ArrayKit;
import org.qfox.jestful.commons.IOKit;
import org.qfox.jestful.commons.ReflectionKit;
import org.qfox.jestful.core.*;
import org.qfox.jestful.core.formatting.RequestDeserializer;
import org.qfox.jestful.core.io.MultipartInputStream;
import org.qfox.jestful.server.JestfulServletRequest;
import org.qfox.jestful.server.converter.ConversionProvider;
import org.qfox.jestful.server.exception.UnsupportedTypeException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

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
 * @date 2016年4月9日 下午6:28:28
 * @since 1.0.0
 */
public class MultipartRequestDeserializer implements RequestDeserializer, Initialable {
    private final Map<MediaType, RequestDeserializer> map = new LinkedHashMap<MediaType, RequestDeserializer>();
    private ConversionProvider multipartConversionProvider;

    public String getContentType() {
        return "multipart/form-data";
    }

    public void deserialize(Action action, MediaType mediaType, String charset, InputStream in) throws IOException {
        String boundary = mediaType.getParameters().get("boundary");
        List<Multipart> multiparts = new ArrayList<Multipart>();
        Map<String, String[]> fields = new LinkedHashMap<String, String[]>();
        List<Parameter> parameters = action.getParameters().all(Position.BODY);
        MultipartInputStream mis = new MultipartInputStream(in, boundary);
        Multihead multihead;
        while ((multihead = mis.getNextMultihead()) != null) {
            Disposition disposition = multihead.getDisposition();
            MediaType type = multihead.getType();
            String encoding = type != null && type.getCharset() != null ? type.getCharset() : charset;
            String name = disposition != null ? disposition.getName() : null;
            Multibody multibody = new Multibody(mis);
            Multipart multipart = new Multipart(multihead, multibody);
            multiparts.add(multipart);
            for (Parameter parameter : parameters) {
                if (!parameter.getName().equals(name)) continue;

                if (parameter.getKlass().isInstance(multihead)) {
                    parameter.setValue(multihead.clone());
                    break;
                }
                if (parameter.getKlass().isInstance(multibody)) {
                    parameter.setValue(multibody.clone());
                    break;
                }
                if (parameter.getKlass().isInstance(multipart)) {
                    parameter.setValue(multipart.clone());
                    break;
                }
                // 数组
                if (parameter.getKlass().isArray() && parameter.getKlass().getComponentType().isInstance(multihead)) {
                    Object[] array = ArrayKit.append((Object[]) parameter.getValue(), parameter.getKlass().getComponentType(), multihead.clone());
                    parameter.setValue(array);
                    break;
                }
                if (parameter.getKlass().isArray() && parameter.getKlass().getComponentType().isInstance(multibody)) {
                    Object[] array = ArrayKit.append((Object[]) parameter.getValue(), parameter.getKlass().getComponentType(), multibody.clone());
                    parameter.setValue(array);
                    break;
                }
                if (parameter.getKlass().isArray() && parameter.getKlass().getComponentType().isInstance(multipart)) {
                    Object[] array = ArrayKit.append((Object[]) parameter.getValue(), parameter.getKlass().getComponentType(), multipart.clone());
                    parameter.setValue(array);
                    break;
                }
                // List Or Collection
                if (ReflectionKit.isListType(parameter.getType(), Multihead.class) || ReflectionKit.isCollectionType(parameter.getType(), Multihead.class)) {
                    List value = (List) parameter.getValue();
                    if (value == null) parameter.setValue(value = new ArrayList());
                    value.add(multihead.clone());
                    break;
                }
                if (ReflectionKit.isListType(parameter.getType(), Multibody.class) || ReflectionKit.isCollectionType(parameter.getType(), Multibody.class)) {
                    List value = (List) parameter.getValue();
                    if (value == null) parameter.setValue(value = new ArrayList());
                    value.add(multibody.clone());
                    break;
                }
                if (ReflectionKit.isListType(parameter.getType(), Multipart.class) || ReflectionKit.isCollectionType(parameter.getType(), Multipart.class)) {
                    List value = (List) parameter.getValue();
                    if (value == null) parameter.setValue(value = new ArrayList());
                    value.add(multipart.clone());
                    break;
                }
                // Set
                if (ReflectionKit.isSetType(parameter.getType(), Multihead.class)) {
                    Set value = (Set) parameter.getValue();
                    if (value == null) parameter.setValue(value = new LinkedHashSet());
                    value.add(multihead.clone());
                    break;
                }
                if (ReflectionKit.isSetType(parameter.getType(), Multibody.class)) {
                    Set value = (Set) parameter.getValue();
                    if (value == null) parameter.setValue(value = new LinkedHashSet());
                    value.add(multibody.clone());
                    break;
                }
                if (ReflectionKit.isSetType(parameter.getType(), Multipart.class)) {
                    Set value = (Set) parameter.getValue();
                    if (value == null) parameter.setValue(value = new LinkedHashSet());
                    value.add(multipart.clone());
                    break;
                }
                // 来到这里证明不是个文件可能是一个Raw Type 也可能是一个 Field Type
                // Raw Type
                if (type != null) {
                    FileInputStream fis = null;
                    try {
                        File file = multibody.getFile();
                        fis = new FileInputStream(file);
                        deserialize(action, parameter, multihead, encoding, fis);
                    } finally {
                        IOKit.close(fis);
                    }
                }
                // Field Type
                else {
                    File file = multibody.getFile();
                    String value = IOKit.toString(file, encoding);
                    String[] values = fields.get(name);
                    if (values == null) {
                        values = new String[]{value};
                    } else {
                        String[] array = new String[values.length + 1];
                        System.arraycopy(values, 0, array, 0, values.length);
                        array[values.length] = value;
                        values = array;
                    }
                    fields.put(name, values);
                }
            }
        }
        FormKit.assign(charset, fields, parameters, multipartConversionProvider);
        Request oldRequest = action.getRequest();
        Request newRequest = new MultipartServletRequest((JestfulServletRequest) oldRequest, fields, multiparts);
        action.setRequest(newRequest);
        mis.close();
    }

    public void deserialize(Action action, Parameter parameter, Multihead multihead, String charset, InputStream in) throws IOException {
        MediaType type = multihead.getType();
        if (map.containsKey(type)) {
            RequestDeserializer deserializer = map.get(type);
            deserializer.deserialize(action, parameter, multihead, charset, in);
        } else {
            String URI = action.getURI();
            String method = action.getRestful().getMethod();
            throw new UnsupportedTypeException(URI, method, type, new Accepts(map.keySet()));
        }
    }

    public void initialize(BeanContainer beanContainer) {
        Collection<RequestDeserializer> deserializers = beanContainer.find(RequestDeserializer.class).values();
        for (RequestDeserializer deserializer : deserializers) {
            String contentType = deserializer.getContentType();
            MediaType mediaType = MediaType.valueOf(contentType);
            map.put(mediaType, deserializer);
        }
        multipartConversionProvider = beanContainer.get(ConversionProvider.class);
    }

    public ConversionProvider getMultipartConversionProvider() {
        return multipartConversionProvider;
    }

    public void setMultipartConversionProvider(ConversionProvider multipartConversionProvider) {
        this.multipartConversionProvider = multipartConversionProvider;
    }

}
