package org.qfox.jestful.tutorial;

import org.qfox.jestful.core.annotation.Body;
import org.qfox.jestful.core.annotation.Jestful;
import org.qfox.jestful.core.annotation.POST;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.io.IOException;

/**
 * Created by yangchangpei on 17/4/11.
 */
@Jestful("/pictures")
@Controller
public class PictureController {

    @POST("/")
    public String upload(@Body("picture") File file) throws IOException {
        System.out.println(file);
        return "status: 302 Found {Location: https://merchant.qfoxy.com/index.jsp}";
    }

}
