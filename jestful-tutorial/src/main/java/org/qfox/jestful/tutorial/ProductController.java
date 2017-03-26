package org.qfox.jestful.tutorial;

import org.qfox.jestful.core.annotation.GET;
import org.qfox.jestful.core.annotation.Jestful;
import org.qfox.jestful.core.annotation.Path;
import org.springframework.stereotype.Controller;

import java.util.Arrays;
import java.util.List;

/**
 * Created by yangchangpei on 16/12/1.
 */
@Jestful("/products")
@Controller
public class ProductController {

    @GET("/{productId:\\d+}")
    public String get(@Path("productId") Long productId) {

        return "forward:/product/detail.jsp";
    }

    @GET(value = "/{productId:\\d+}.json", produces = "application/json")
    public List<Long> json(@Path("productId") Long productId) {
        return Arrays.asList(productId);
    }

}
