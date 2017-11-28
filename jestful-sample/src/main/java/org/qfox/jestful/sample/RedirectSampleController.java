package org.qfox.jestful.sample;

import org.qfox.jestful.core.annotation.*;
import org.qfox.jestful.sample.user.User;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Jestful("/redirect")
@Controller
public class RedirectSampleController {

    @GET("/source")
    public void getSource(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println(request.getRemotePort());
        System.out.flush();
        response.addHeader("Location", "http://localhost:8080/redirect/target?name=Payne");
        response.sendError(301);
    }

    @GET("/target")
    public User getTarget(HttpServletRequest request, @Query("name") String name) {
        System.out.println(request.getRemotePort());
        System.out.flush();
        return new User("Payne", 1L, null, null);
    }

    @POST("/source")
    public void postSource(@Query("job") String job, @Body User user, HttpServletResponse response) throws IOException {
        response.addHeader("Location", "http://localhost:8080/redirect/target?name=Payne");
        response.sendError(307);
    }

    @POST("/target")
    public User postTarget(@Query("job") String job, @Query("name") String name, @Body User user, HttpServletResponse response) throws IOException {
        return new User("Payne", 1L, null, null);
    }

}
