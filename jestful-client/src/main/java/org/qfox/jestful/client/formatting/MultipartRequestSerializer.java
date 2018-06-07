package org.qfox.jestful.client.formatting;

import eu.medsea.mimeutil.MimeUtil;
import org.qfox.jestful.client.Part;
import org.qfox.jestful.client.exception.NoSuchSerializerException;
import org.qfox.jestful.commons.IOKit;
import org.qfox.jestful.commons.ReflectionKit;
import org.qfox.jestful.commons.StringKit;
import org.qfox.jestful.core.*;
import org.qfox.jestful.core.exception.JestfulIOException;
import org.qfox.jestful.core.formatting.RequestSerializer;
import org.qfox.jestful.core.io.MultipartOutputStream;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
 * @date 2016年5月4日 下午3:52:47
 * @since 1.0.0
 */
public class MultipartRequestSerializer implements RequestSerializer, Initialable {
    private final Map<MediaType, RequestSerializer> map = new LinkedHashMap<MediaType, RequestSerializer>();
    private final String contentType = "multipart/form-data";

    public String getContentType() {
        return contentType;
    }

    public boolean supports(Action action) {
        List<Parameter> bodies = action.getParameters().all(Position.BODY);
        return bodies.size() == 0 || bodies.size() > 1 || supports(bodies.get(0));
    }

    public boolean supports(Parameter parameter) {
        Type type = parameter.getType();
        return File.class == type
                || Part.class == type
                || ReflectionKit.isArrayType(type, File.class)
                || ReflectionKit.isListType(type, File.class)
                || ReflectionKit.isSetType(type, File.class)
                || ReflectionKit.isCollectionType(type, File.class)
                || ReflectionKit.isMapType(type, String.class, File.class)
                || ReflectionKit.isMapArrayType(type, String.class, File.class)
                || ReflectionKit.isMapListType(type, String.class, File.class)
                || ReflectionKit.isMapSetType(type, String.class, File.class)
                || ReflectionKit.isMapCollectionType(type, String.class, File.class)
                || ReflectionKit.isArrayType(type, Part.class)
                || ReflectionKit.isListType(type, Part.class)
                || ReflectionKit.isSetType(type, Part.class)
                || ReflectionKit.isCollectionType(type, Part.class)
                || ReflectionKit.isMapType(type, String.class, Part.class)
                || ReflectionKit.isMapArrayType(type, String.class, Part.class)
                || ReflectionKit.isMapListType(type, String.class, Part.class)
                || ReflectionKit.isMapSetType(type, String.class, Part.class)
                || ReflectionKit.isMapCollectionType(type, String.class, Part.class);
    }

    public void serialize(Action action, String charset, OutputStream out) throws IOException {
        List<Parameter> bodies = action.getParameters().all(Position.BODY);
        Accepts consumes = action.getConsumes();
        String nonce = StringKit.random(16);
        String boundary = "----JestfulFormBoundary" + nonce;
        action.getRequest().setContentType(contentType + "; boundary=" + boundary);
        MultipartOutputStream mos = null;
        try {
            mos = new MultipartOutputStream(out, boundary);
            flag:
            for (Parameter body : bodies) {
                {
                    MediaType mediaType = MediaType.valueOf(contentType);
                    RequestSerializer serializer = this;
                    if ((consumes.isEmpty() || consumes.contains(mediaType)) && serializer.supports(body)) {
                        serializer.serialize(action, body, charset, mos);
                        continue;
                    }
                }
                for (Entry<MediaType, RequestSerializer> entry : map.entrySet()) {
                    MediaType mediaType = entry.getKey();
                    RequestSerializer serializer = entry.getValue();
                    if ((consumes.isEmpty() || consumes.contains(mediaType)) && serializer.supports(body)) {
                        serializer.serialize(action, body, charset, mos);
                        continue flag;
                    }
                }
                throw new NoSuchSerializerException(action, body, consumes, map.values());
            }
            mos.flush();
        } catch (NoSuchSerializerException e) {
            throw new JestfulIOException(e);
        } finally {
            if (mos != null) mos.close(true);
        }
    }

    public void serialize(Action action, Parameter parameter, String charset, MultipartOutputStream out) throws IOException {
        Type type = parameter.getType();
        Object value = parameter.getValue();
        String name = parameter.getName();
        if (type == null) {
            throw new IOException();
        } else if (File.class == type) {
            if (value == null) return;
            serialize(out, (File) value, name);
        } else if (ReflectionKit.isArrayType(type, File.class)) {
            if (value == null) return;
            int length = Array.getLength(value);
            for (int i = 0; i < length; i++) serialize(out, (File) Array.get(value, i), name);
        } else if (ReflectionKit.isListType(type, File.class) || ReflectionKit.isSetType(type, File.class) || ReflectionKit.isCollectionType(type, File.class)) {
            if (value == null) return;
            Iterable<?> iterable = (Iterable<?>) value;
            for (Object item : iterable) serialize(out, (File) item, name);
        } else if (ReflectionKit.isMapType(type, String.class, File.class)) {
            if (value == null) return;
            Map<?, ?> map = (Map<?, ?>) value;
            String prefix = name.trim().equals(String.valueOf(parameter.getIndex())) ? "" : name.trim() + ".";
            for (Entry<?, ?> entry : map.entrySet()) serialize(out, (File) entry.getValue(), prefix + String.valueOf(entry.getKey()));
        } else if (ReflectionKit.isMapArrayType(type, String.class, File.class)) {
            if (value == null) return;
            Map<?, ?> map = (Map<?, ?>) value;
            String prefix = name.trim().equals(String.valueOf(parameter.getIndex())) ? "" : name.trim() + ".";
            for (Entry<?, ?> entry : map.entrySet()) {
                Object array = entry.getValue();
                int length = Array.getLength(array);
                for (int i = 0; i < length; i++) serialize(out, (File) Array.get(array, i), prefix + String.valueOf(entry.getKey()));
            }
        } else if (ReflectionKit.isMapListType(type, String.class, File.class) || ReflectionKit.isMapSetType(type, String.class, File.class) || ReflectionKit.isMapCollectionType(type, String.class, File.class)) {
            if (value == null) return;
            Map<?, ?> map = (Map<?, ?>) value;
            String prefix = name.trim().equals(String.valueOf(parameter.getIndex())) ? "" : name.trim() + ".";
            for (Entry<?, ?> entry : map.entrySet()) {
                Iterable<?> iterable = (Iterable<?>) entry.getValue();
                for (Object item : iterable) serialize(out, (File) item, prefix + String.valueOf(entry.getKey()));
            }
        } else if (Part.class == type) {
            if (value == null) return;
            serialize(out, (Part) value, name);
        } else if (ReflectionKit.isArrayType(type, Part.class)) {
            if (value == null) return;
            int length = Array.getLength(value);
            for (int i = 0; i < length; i++) serialize(out, (Part) Array.get(value, i), name);
        } else if (ReflectionKit.isListType(type, Part.class) || ReflectionKit.isSetType(type, Part.class) || ReflectionKit.isCollectionType(type, Part.class)) {
            if (value == null) return;
            Iterable<?> iterable = (Iterable<?>) value;
            for (Object item : iterable) serialize(out, (Part) item, name);
        } else if (ReflectionKit.isMapType(type, String.class, Part.class)) {
            if (value == null) return;
            Map<?, ?> map = (Map<?, ?>) value;
            String prefix = name.trim().equals(String.valueOf(parameter.getIndex())) ? "" : name.trim() + ".";
            for (Entry<?, ?> entry : map.entrySet()) serialize(out, (Part) entry.getValue(), prefix + String.valueOf(entry.getKey()));
        } else if (ReflectionKit.isMapArrayType(type, String.class, Part.class)) {
            if (value == null) return;
            Map<?, ?> map = (Map<?, ?>) value;
            String prefix = name.trim().equals(String.valueOf(parameter.getIndex())) ? "" : name.trim() + ".";
            for (Entry<?, ?> entry : map.entrySet()) {
                Object array = entry.getValue();
                int length = Array.getLength(array);
                for (int i = 0; i < length; i++) serialize(out, (Part) Array.get(array, i), prefix + String.valueOf(entry.getKey()));
            }
        } else if (ReflectionKit.isMapListType(type, String.class, Part.class) || ReflectionKit.isMapSetType(type, String.class, Part.class) || ReflectionKit.isMapCollectionType(type, String.class, Part.class)) {
            if (value == null) return;
            Map<?, ?> map = (Map<?, ?>) value;
            String prefix = name.trim().equals(String.valueOf(parameter.getIndex())) ? "" : name.trim() + ".";
            for (Entry<?, ?> entry : map.entrySet()) {
                Iterable<?> iterable = (Iterable<?>) entry.getValue();
                for (Object item : iterable) serialize(out, (Part) item, prefix + String.valueOf(entry.getKey()));
            }
        }
    }

    private void serialize(MultipartOutputStream mos, File file, String name) throws IOException {
        String filename = file.getName();
        Disposition disposition = new Disposition("form-data", name, filename);
        Collection<?> mediaTypes = MimeUtil.getMimeTypes(file);
        String contentType = mediaTypes.isEmpty() ? "application/octet-stream" : mediaTypes.toArray()[0].toString();
        MediaType mediaType = MediaType.valueOf(contentType);
        Multihead multihead = new Multihead(disposition, mediaType);
        mos.setNextMultihead(multihead);
        IOKit.transfer(file, mos);
    }

    private void serialize(MultipartOutputStream mos, Part part, String name) throws IOException {
        Disposition disposition = new Disposition("form-data", name);
        String contentType = part.getContentType();
        MediaType mediaType = contentType != null ? MediaType.valueOf(contentType) : MediaType.valueOf("application/octet-stream");
        Multihead multihead = new Multihead(disposition, mediaType);
        mos.setNextMultihead(multihead);
        part.writeTo(mos);
    }

    public void initialize(BeanContainer beanContainer) {
        Collection<RequestSerializer> serializers = beanContainer.find(RequestSerializer.class).values();
        for (RequestSerializer serializer : serializers) {
            String contentType = serializer.getContentType();
            MediaType mediaType = MediaType.valueOf(contentType);
            map.put(mediaType, serializer);
        }
    }

}
