package org.qfox.elasticsearch.node;

import org.qfox.jestful.core.http.GET;
import org.qfox.jestful.core.http.HTTP;
import rx.Observable;

/**
 * 节点API
 *
 * @author Payne 646742615@qq.com
 * 2019/8/16 17:36
 */
@HTTP
public interface NodeAPI {

    @GET
    Observable<NodeStatusResult> status();

    @GET("/_count")
    Observable<NodeCountResult> count();

}
