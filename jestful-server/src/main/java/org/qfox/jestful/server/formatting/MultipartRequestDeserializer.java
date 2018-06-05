package org.qfox.jestful.server.formatting;

import org.qfox.jestful.commons.ArrayKit;
import org.qfox.jestful.commons.IOKit;
import org.qfox.jestful.commons.ReflectionKit;
import org.qfox.jestful.commons.conversion.ConversionProvider;
import org.qfox.jestful.core.*;
import org.qfox.jestful.core.formatting.RequestDeserializer;
import org.qfox.jestful.core.io.MultipartInputStream;
import org.qfox.jestful.server.JestfulServletRequest;
import org.qfox.jestful.server.annotation.Field;
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
    private final Map<MediaType, RequestDeserializer> deserializers = new LinkedHashMap<MediaType, RequestDeserializer>();
    private ConversionProvider multipartConversionProvider;

    public String getContentType() {
        return "multipart/form-data";
    }

    public void deserialize(Action action, MediaType mediaType, String charset, InputStream in) throws IOException {
        String boundary = mediaType.getParameters().get("boundary");
        List<Multipart> multiparts = new ArrayList<Multipart>();
        Map<String, String[]> map = new LinkedHashMap<String, String[]>();
        // 读取
        MultipartInputStream mis = new MultipartInputStream(in, boundary);
        try {
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

        List<Parameter> bodies = action.getParameters().all(Position.BODY, true);
        for (Parameter parameter : bodies) {
            for (Multipart multipart : multiparts) {
                Multihead multihead = multipart.getMultihead();
                Multibody multibody = multipart.getMultibody();
                String name = multipart.getName();
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
                    Object[] array = ArrayKit.append((Object[]) parameter.getValue(), (Class) parameter.getKlass().getComponentType(), multihead.clone());
                    parameter.setValue(array);
                    break;
                }
                if (parameter.getKlass().isArray() && parameter.getKlass().getComponentType().isInstance(multibody)) {
                    Object[] array = ArrayKit.append((Object[]) parameter.getValue(), (Class) parameter.getKlass().getComponentType(), multibody.clone());
                    parameter.setValue(array);
                    break;
                }
                if (parameter.getKlass().isArray() && parameter.getKlass().getComponentType().isInstance(multipart)) {
                    Object[] array = ArrayKit.append((Object[]) parameter.getValue(), (Class) parameter.getKlass().getComponentType(), multipart.clone());
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
                FileInputStream fis = null;
                try {
                    File file = multibody.getFile();
                    fis = new FileInputStream(file);
                    deserialize(action, parameter, multihead, charset, fis);
                } finally {
                    IOKit.close(fis);
                }
            }
        }

        List<Parameter> fields = action.getParameters().all(Field.POSITION, true);
        FormKit.assign(charset, map, fields, multipartConversionProvider);
        Request oldRequest = action.getRequest();
        Request newRequest = new MultipartServletRequest((JestfulServletRequest) oldRequest, map, multiparts);
        action.setRequest(newRequest);
    }

    public void deserialize(Action action, Parameter parameter, Multihead multihead, String charset, InputStream in) throws IOException {
        MediaType mediaType = multihead.getType();
        if (mediaType == null) return;
        Request request = action.getRequest();
        Accepts consumes = action.getConsumes();
        Accepts supports = new Accepts(deserializers.keySet());
        if (supports.contains(mediaType) && (consumes.isEmpty() || consumes.contains(mediaType))) {
            charset = mediaType.getCharset() != null ? mediaType.getCharset() : charset;
            if (charset == null || charset.length() == 0) charset = request.getRequestHeader("Content-Charset");
            if (charset == null || charset.length() == 0) charset = request.getCharacterEncoding();
            if (charset == null || charset.length() == 0) charset = java.nio.charset.Charset.defaultCharset().name();
            RequestDeserializer deserializer = deserializers.get(mediaType);
            deserializer.deserialize(action, parameter, multihead, charset, in);
        } else {
            String URI = action.getRequestURI();
            String method = action.getRestful().getMethod();
            if (!consumes.isEmpty()) supports.retainAll(consumes);
            throw new UnsupportedTypeException(URI, method, mediaType, supports);
        }
    }

    public void initialize(BeanContainer beanContainer) {
        Collection<RequestDeserializer> deserializers = beanContainer.find(RequestDeserializer.class).values();
        for (RequestDeserializer deserializer : deserializers) {
            String contentType = deserializer.getContentType();
            MediaType mediaType = MediaType.valueOf(contentType);
            this.deserializers.put(mediaType, deserializer);
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
