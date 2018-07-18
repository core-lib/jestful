package org.qfox.jestful.sample;

import org.qfox.jestful.core.http.*;
import org.springframework.stereotype.Controller;

import java.util.Map;

@HTTP("/")
@Controller
public class IndexController {

    @GET("/")
    public String index() {
        return "@forward:/index.jsp";
    }

    @POST("/{路径}/{矩阵}")
    public String matrix(
            @Path("路径") String path,
            @Path("矩阵") String matrix,
            @Matrix(value = "姓名", path = "路径") String[] names,
            @Query("查询") String query,
            @Header("请求头") String header,
            @Cookie("饼干") String cookie,
            @Body Map<String, String> remark
    ) {

        return "@:OK";
    }

}
