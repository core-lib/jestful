package org.qfox.jestful.server.acquirer;

import org.qfox.jestful.core.*;
import org.qfox.jestful.core.exception.JestfulRuntimeException;
import org.qfox.jestful.server.JestfulServletRequest;
import org.qfox.jestful.server.formatting.FormKit;
import org.qfox.jestful.server.formatting.Multipart;
import org.qfox.jestful.server.formatting.MultipartServletRequest;
import org.springframework.web.multipart.MultipartRequest;

import java.io.InputStream;
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
        try {
            if (!MultipartRequest.class.isAssignableFrom(parameter.getKlass())) return null;

            Parameters parameters = action.getParameters();
            if (parameters.count(Position.BODY) > 0) return null;
            Request request = action.getRequest();
            String contentType = request.getContentType();
            MediaType mediaType = MediaType.valueOf(contentType);
            if (!MediaType.valueOf("multipart/form-data").matches(mediaType)) return null;

            InputStream in = request.getRequestInputStream();
            String boundary = mediaType.getParameters().get("boundary");
            List<Multipart> multiparts = new ArrayList<Multipart>();
            Map<String, String[]> map = new LinkedHashMap<String, String[]>();
            FormKit.extract(in, boundary, multiparts, map);
            Request newRequest = new MultipartServletRequest((JestfulServletRequest) request, map, multiparts);
            action.setRequest(newRequest);
            return newRequest;
        } catch (Exception e) {
            throw new JestfulRuntimeException(e);
        }
    }

}
