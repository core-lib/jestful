package org.qfox.jestful.sample;

import org.qfox.jestful.core.annotation.Body;
import org.qfox.jestful.core.annotation.GET;
import org.qfox.jestful.core.annotation.Jestful;
import org.qfox.jestful.core.annotation.POST;
import org.qfox.jestful.sample.user.User;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Jestful("/redirect")
@Controller
public class RedirectSampleController {

    @GET("/source")
    public String getSource() {
        return "@redirect:/redirect/target";
    }

    @GET("/target")
    public User getTarget(){
        return new User("Payne", 1L, null, null);
    }

    @POST("/source")
    public void postSource(@Body User user, HttpServletResponse response) throws IOException {
        response.addHeader("Location", "http://localhost:8080/redirect/target");
        response.sendError(307);
    }

    @POST("/target")
    public User postTarget(@Body User user, HttpServletResponse response) throws IOException {
        return new User("Payne", 1L, null, null);
    }

}
