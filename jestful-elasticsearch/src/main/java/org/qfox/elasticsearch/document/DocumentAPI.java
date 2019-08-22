package org.qfox.elasticsearch.document;

import org.qfox.jestful.core.http.*;

/**
 * 文档API
 *
 * @author Payne 646742615@qq.com
 * 2019/8/19 9:47
 */
@HTTP
public interface DocumentAPI {

    @PUT("/{index}/{type}/{id}")
    DocumentIndexResult index(@Path("index") String index, @Path("type") String type, @Path("id") String id, @Body Object document);

    @POST("/{index}/{type}")
    DocumentIndexResult create(@Path("index") String index, @Path("type") String type, @Body Object document);

}
