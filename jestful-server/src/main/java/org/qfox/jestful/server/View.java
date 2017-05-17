package org.qfox.jestful.server;

import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Request;
import org.qfox.jestful.core.Response;

import javax.servlet.ServletContext;

/**
 * Created by yangchangpei on 17/5/17.
 */
public interface View {

    boolean supports(Action action, String extension);

    void render(ServletContext context, String path, Action action, Request request, Response response) throws Exception;

}
