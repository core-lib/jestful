package org.qfox.elasticsearch.document;

import org.qfox.jestful.core.http.*;
import rx.Observable;

/**
 * 文档API
 *
 * @author Payne 646742615@qq.com
 * 2019/8/19 9:47
 */
@HTTP
public interface DocumentAPI<T> {

    @PUT("/{index}/{type}/{id}")
    Observable<DocumentIndexResult> index(@Path("index") String index, @Path("type") String type, @Path("id") String id, @Body T document);

    @POST("/{index}/{type}")
    Observable<DocumentIndexResult> create(@Path("index") String index, @Path("type") String type, @Body T document);

    @HEAD("/{index}/{type}/{id}")
    Observable<Boolean> exists(@Path("index") String index, @Path("type") String type, @Path("id") String id);

    @DELETE("/{index}/{type}/{id}")
    Observable<DocumentDeleteResult> delete(@Path("index") String index, @Path("type") String type, @Path("id") String id);

    @GET("/{index}/{type}/{id}")
    Observable<DocumentQueryResult<T>> query(@Path("index") String index, @Path("type") String type, @Path("id") String id);

}
