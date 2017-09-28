package org.qfox.jestful.client.aio.catcher;

import org.qfox.jestful.client.aio.AioClient;
import org.qfox.jestful.client.catcher.Catcher;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.exception.StatusException;

/**
 * Created by payne on 2017/4/8.
 * Version: 1.0
 */
public interface AioCatcher extends Catcher {

    void aioCaught(AioClient client, Action action, StatusException statusException) throws Exception;

}
