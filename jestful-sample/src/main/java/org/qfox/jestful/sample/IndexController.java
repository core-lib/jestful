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

    @GET("/{路径}/{矩阵}")
    public String matrix(
            @Path("路径") String path,
            @Path("矩阵") String matrix,
            @Matrix(value = "姓名", path = "路径") String[] names,
            @Query("查询") String[] queries,
            @Header("请求头") String[] headers,
            @Cookie("饼干") String[] cookies,
            @Body Map<String, String> remark
    ) {

        return "@:OK";
    }

}
