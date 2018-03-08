package org.qfox.jestful.weixin.media;

import org.qfox.jestful.client.scheduler.Callback;
import org.qfox.jestful.client.scheduler.OnCompleted;
import org.qfox.jestful.client.scheduler.OnFail;
import org.qfox.jestful.client.scheduler.OnSuccess;
import org.qfox.jestful.core.http.*;

import java.io.File;

/**
 * 微信公众号永久素材API
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-03-08 13:31
 **/
@HTTP("/cgi-bin/material")
public interface MaterialAPI {

    @POST(value = "/add_news", consumes = "application/json", produces = "application/json")
    NewsUploadResult upload(
            @Query("access_token") String token,
            @Body News news
    );

    @POST(value = "/add_material", consumes = {"multipart/form-data", "application/json"}, produces = "application/json")
    MaterialUploadResult upload(
            @Query("access_token") String token,
            @Query("type") MediaType type,
            @Body("media") File media,
            @Body("description") Description description
    );

    @POST(value = "/add_material", consumes = {"multipart/form-data", "application/json"}, produces = "application/json")
    void upload(
            @Query("access_token") String token,
            @Query("type") MediaType type,
            @Body("media") File media,
            @Body("description") Description description,
            Callback<MaterialUploadResult> callback
    );

    @POST(value = "/add_material", consumes = {"multipart/form-data", "application/json"}, produces = "application/json")
    void upload(
            @Query("access_token") String token,
            @Query("type") MediaType type,
            @Body("media") File media,
            @Body("description") Description description,
            OnCompleted<MaterialUploadResult> onCompleted
    );

    @POST(value = "/add_material", consumes = {"multipart/form-data", "application/json"}, produces = "application/json")
    void upload(
            @Query("access_token") String token,
            @Query("type") MediaType type,
            @Body("media") File media,
            @Body("description") Description description,
            OnSuccess<MaterialUploadResult> onSuccess
    );

    @POST(value = "/add_material", consumes = {"multipart/form-data", "application/json"}, produces = "application/json")
    void upload(
            @Query("access_token") String token,
            @Query("type") MediaType type,
            @Body("media") File media,
            @Body("description") Description description,
            OnSuccess<MaterialUploadResult> onSuccess,
            OnFail onFail
    );

    @POST(value = "/add_material", consumes = {"multipart/form-data", "application/json"}, produces = "application/json")
    void upload(
            @Query("access_token") String token,
            @Query("type") MediaType type,
            @Body("media") File media,
            @Body("description") Description description,
            OnSuccess<MaterialUploadResult> onSuccess,
            OnFail onFail,
            OnCompleted<MaterialUploadResult> onCompleted
    );

    @POST(value = "/get_material", consumes = "application/json", produces = "application/json")
    MaterialQueryResult query(
            @Query("access_token") String token,
            @Body Material material
    );

    @POST(value = "/get_material", consumes = "application/json")
    File download(
            @Query("access_token") String token,
            @Body Material material
    );

    @POST(value = "/get_material", consumes = "application/json")
    void download(
            @Query("access_token") String token,
            @Body Material material,
            Callback<File> callback
    );

    @POST(value = "/get_material", consumes = "application/json")
    void download(
            @Query("access_token") String token,
            @Body Material material,
            OnCompleted<File> onCompleted
    );

    @POST(value = "/get_material", consumes = "application/json")
    void download(
            @Query("access_token") String token,
            @Body Material material,
            OnSuccess<File> onSuccess
    );

    @POST(value = "/get_material", consumes = "application/json")
    void download(
            @Query("access_token") String token,
            @Body Material material,
            OnSuccess<File> onSuccess,
            OnFail onFail
    );

    @POST(value = "/get_material", consumes = "application/json")
    void download(
            @Query("access_token") String token,
            @Body Material material,
            OnSuccess<File> onSuccess,
            OnFail onFail,
            OnCompleted<File> onCompleted
    );

    @POST(value = "/del_material", consumes = "application/json", produces = "application/json")
    MaterialDeleteResult delete(
            @Query("access_token") String token,
            @Body Material material
    );

    @GET(value = "/get_materialcount", produces = "application/json")
    MaterialCountResult count(@Query("access_token") String token);

    @POST(value = "/batchget_material", consumes = "application/json", produces = "application/json")
    MaterialListResult list(
            @Query("access_token") String token,
            @Body Pagination pagination
    );

}
