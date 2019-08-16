package org.qfox.elasticsearch.node;

import org.qfox.jestful.core.http.GET;
import org.qfox.jestful.core.http.HTTP;

/**
 * 节点API
 *
 * @author Payne 646742615@qq.com
 * 2019/8/16 17:36
 */
@HTTP
public interface NodeAPI {

    @GET
    NodeStatusResult status();

    @GET("/_count")
    NodeCountResult count();

}
