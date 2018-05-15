package org.qfox.jestful.server.acquirer;

import org.qfox.jestful.commons.IOKit;
import org.qfox.jestful.core.*;
import org.qfox.jestful.core.exception.JestfulRuntimeException;
import org.qfox.jestful.core.io.MultipartInputStream;
import org.qfox.jestful.server.JestfulServletRequest;
import org.qfox.jestful.server.formatting.Multipart;
import org.qfox.jestful.server.formatting.MultipartServletRequest;
import org.springframework.web.multipart.MultipartRequest;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
 * @date 2016年4月19日 下午3:54:05
 * @since 1.0.0
 */
public class RequestParameterAcquirer implements Acquirer {

    public Object acquire(Action action, Parameter parameter) {
        if (parameter.getKlass().isInstance(action.getRequest())) {
            return action.getRequest();
        }
        MultipartInputStream mis = null;
        try {
            if (!MultipartRequest.class.isAssignableFrom(parameter.getKlass())) return null;

            Parameters parameters = action.getParameters();
            if (parameters.count(Position.BODY) > 0) return null;
            Request request = action.getRequest();
            String contentType = request.getContentType();
            MediaType mediaType = MediaType.valueOf(contentType);
            if (!MediaType.valueOf("multipart/form-data").matches(mediaType)) return null;

            String boundary = mediaType.getParameters().get("boundary");
            List<Multipart> multiparts = new ArrayList<Multipart>();
            Map<String, String[]> fields = new LinkedHashMap<String, String[]>();
            mis = new MultipartInputStream(request.getRequestInputStream(), boundary);
            Multihead multihead;
            while ((multihead = mis.getNextMultihead()) != null) {
                Disposition disposition = multihead.getDisposition();
                MediaType type = multihead.getType();
                String name = disposition.getName();
                if (type != null) {
                    Multibody multibody = new Multibody(mis);
                    Multipart multipart = new Multipart(multihead, multibody);
                    multiparts.add(multipart);
                } else {
                    String value = IOKit.toString(mis);
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
            Request newRequest = new MultipartServletRequest((JestfulServletRequest) request, fields, multiparts);
            action.setRequest(newRequest);
            return newRequest;
        } catch (Exception e) {
            throw new JestfulRuntimeException(e);
        } finally {
            IOKit.close(mis);
        }
    }

}
