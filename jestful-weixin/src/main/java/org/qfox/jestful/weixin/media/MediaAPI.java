package org.qfox.jestful.weixin.media;

import org.qfox.jestful.client.scheduler.Callback;
import org.qfox.jestful.client.scheduler.OnCompleted;
import org.qfox.jestful.client.scheduler.OnFail;
import org.qfox.jestful.client.scheduler.OnSuccess;
import org.qfox.jestful.core.http.*;

import java.io.File;

/**
 * 微信公众号媒体管理API
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-03-08 11:02
 **/
@HTTP("/cgi-bin/media")
public interface MediaAPI {

    //------------------------------- 素材管理接口 Start --------------------------------//
    @POST(value = "/upload", consumes = "multipart/form-data", produces = "application/json")
    MediaUploadResult upload(
            @Query("access_token") String token,
            @Query("type") MediaType type,
            @Body("media") File image
    );

    @POST(value = "/upload", consumes = "multipart/form-data", produces = "application/json")
    void upload(
            @Query("access_token") String token,
            @Query("type") MediaType type,
            @Body("media") File image,
            Callback<MediaUploadResult> callback
    );

    @POST(value = "/upload", consumes = "multipart/form-data", produces = "application/json")
    void upload(
            @Query("access_token") String token,
            @Query("type") MediaType type,
            @Body("media") File image,
            OnCompleted<MediaUploadResult> onCompleted
    );

    @POST(value = "/upload", consumes = "multipart/form-data", produces = "application/json")
    void upload(
            @Query("access_token") String token,
            @Query("type") MediaType type,
            @Body("media") File image,
            OnSuccess<MediaUploadResult> onSuccess
    );

    @POST(value = "/upload", consumes = "multipart/form-data", produces = "application/json")
    void upload(
            @Query("access_token") String token,
            @Query("type") MediaType type,
            @Body("media") File image,
            OnSuccess<MediaUploadResult> onSuccess,
            OnFail onFail
    );

    @POST(value = "/upload", consumes = "multipart/form-data", produces = "application/json")
    void upload(
            @Query("access_token") String token,
            @Query("type") MediaType type,
            @Body("media") File image,
            OnSuccess<MediaUploadResult> onSuccess,
            OnFail onFail,
            OnCompleted<MediaUploadResult> onCompleted
    );

    @GET("/get")
    File download(
            @Query("access_token") String token,
            @Query("media_id") String media_id
    );

    @GET("/get")
    void download(
            @Query("access_token") String token,
            @Query("media_id") String media_id,
            Callback<File> callback
    );

    @GET("/get")
    void download(
            @Query("access_token") String token,
            @Query("media_id") String media_id,
            OnCompleted<File> onCompleted
    );

    @GET("/get")
    void download(
            @Query("access_token") String token,
            @Query("media_id") String media_id,
            OnSuccess<File> onSuccess
    );

    @GET("/get")
    void download(
            @Query("access_token") String token,
            @Query("media_id") String media_id,
            OnSuccess<File> onSuccess,
            OnFail onFail
    );

    @GET("/get")
    void download(
            @Query("access_token") String token,
            @Query("media_id") String media_id,
            OnSuccess<File> onSuccess,
            OnFail onFail,
            OnCompleted<File> onCompleted
    );

    //------------------------------- 素材管理接口 End --------------------------------//

    @POST(value = "/uploadimg", consumes = "multipart/form-data", produces = "application/json")
    ImageUploadResult upload(
            @Query("access_token") String token,
            @Body("media") File image
    );

    @POST(value = "/uploadimg", consumes = "multipart/form-data", produces = "application/json")
    void upload(
            @Query("access_token") String token,
            @Body("media") File image,
            Callback<ImageUploadResult> callback
    );

    @POST(value = "/uploadimg", consumes = "multipart/form-data", produces = "application/json")
    void upload(
            @Query("access_token") String token,
            @Body("media") File image,
            OnCompleted<ImageUploadResult> onCompleted
    );

    @POST(value = "/uploadimg", consumes = "multipart/form-data", produces = "application/json")
    void upload(
            @Query("access_token") String token,
            @Body("media") File image,
            OnSuccess<ImageUploadResult> onSuccess
    );

    @POST(value = "/uploadimg", consumes = "multipart/form-data", produces = "application/json")
    void upload(
            @Query("access_token") String token,
            @Body("media") File image,
            OnSuccess<ImageUploadResult> onSuccess,
            OnFail onFail
    );

    @POST(value = "/uploadimg", consumes = "multipart/form-data", produces = "application/json")
    void upload(
            @Query("access_token") String token,
            @Body("media") File image,
            OnSuccess<ImageUploadResult> onSuccess,
            OnFail onFail,
            OnCompleted<ImageUploadResult> onCompleted
    );

    @POST(value = "/uploadnews", consumes = "application/json", produces = "application/json")
    NewsUploadResult upload(
            @Query("access_token") String token,
            @Body News news
    );

}
