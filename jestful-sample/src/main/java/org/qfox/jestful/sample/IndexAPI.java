package org.qfox.jestful.sample;

import org.qfox.jestful.core.http.*;

@HTTP("/")
public interface IndexAPI {

    @GET("/")
    String index();

    @GET("/{路径}/{矩阵}")
    String matrix(
            @Path("路径") String path,
            @Path("矩阵") String matrix,
            @Matrix(value = "姓名", path = "路径") String[] names,
            @Query("查询") String[] queries,
            @Header("请求头") String[] headers,
            @Cookie("饼干") String[] cookies
    );

}
