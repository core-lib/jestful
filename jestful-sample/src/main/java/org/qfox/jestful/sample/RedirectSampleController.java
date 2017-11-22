package org.qfox.jestful.sample;

import org.qfox.jestful.core.annotation.*;
import org.qfox.jestful.sample.user.User;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Jestful("/redirect")
@Controller
public class RedirectSampleController {

    @GET("/source")
    public String getSource() {
        return "@redirect:/redirect/target?name=Payne";
    }

    @GET("/target")
    public User getTarget(@Query("name") String name){
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
