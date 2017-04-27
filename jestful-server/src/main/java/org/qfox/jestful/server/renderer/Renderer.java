package org.qfox.jestful.server.renderer;

import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Request;
import org.qfox.jestful.core.Response;

/**
 * Created by Payne on 2017/4/27.
 */
public interface Renderer {

    boolean supports(Action action, Object value);

    void render(Action action, Object value, Request request, Response response) throws Exception;

}
