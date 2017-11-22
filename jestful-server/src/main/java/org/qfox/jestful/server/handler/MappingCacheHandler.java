package org.qfox.jestful.server.handler;

import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Actor;
import org.qfox.jestful.core.Mapping;
import org.qfox.jestful.core.Response;
import org.qfox.jestful.server.annotation.Cache;

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
 * @date 2016年5月31日 下午4:35:48
 * @since 1.0.0
 */
public class MappingCacheHandler implements Actor {

    public Object react(Action action) throws Exception {
        Mapping mapping = action.getMapping();
        Response response = action.getResponse();
        if (mapping.isAnnotationPresent(Cache.class)) {
            Cache cache = mapping.getAnnotation(Cache.class);
            String[] values = cache.value();
            String control = "";
            for (String value : values) {
                if (control.length() == 0) {
                    control = value.trim();
                } else {
                    control += ", " + value.trim();
                }
            }
            if (control.length() == 0 == false) {
                response.setResponseHeader("Cache-Control", control);
            }
            String[] varies = cache.vary();
            String vary = "";
            for (String v : varies) {
                if (vary.length() == 0) {
                    vary = v.trim();
                } else {
                    vary += ", " + v.trim();
                }
            }
            if (vary.length() == 0 == false) {
                response.setResponseHeader("Vary", vary);
            }
        }
        return action.execute();
    }

}
