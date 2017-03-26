package org.qfox.jestful.tutorial;

import org.qfox.jestful.core.annotation.Cookie;
import org.qfox.jestful.core.annotation.GET;
import org.qfox.jestful.core.annotation.Jestful;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by payne on 2017/3/26.
 */
@Jestful("/cookie")
@Controller
public class CookieController {

    @GET("/")
    public String index(@Cookie("cookie") String cookie, HttpServletResponse response) {
        if (cookie != null) {
            System.out.println(cookie);
        } else {
            response.addCookie(new javax.servlet.http.Cookie("cookie", "test"));
        }
        return "@:ok";
    }

}