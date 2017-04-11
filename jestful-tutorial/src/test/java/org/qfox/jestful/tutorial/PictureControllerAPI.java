package org.qfox.jestful.tutorial;

import org.qfox.jestful.client.scheduler.OnCompleted;
import org.qfox.jestful.core.annotation.Body;
import org.qfox.jestful.core.annotation.Jestful;
import org.qfox.jestful.core.annotation.POST;

import java.io.File;

/**
 * Created by yangchangpei on 17/4/11.
 */
@Jestful("/pictures")
public interface PictureControllerAPI {

    @POST(value = "/", consumes = "multipart/form-data")
    void upload(@Body("picture") File file, OnCompleted<String> onCompleted);

}
