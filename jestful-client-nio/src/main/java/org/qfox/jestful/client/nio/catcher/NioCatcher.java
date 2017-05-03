package org.qfox.jestful.client.nio.catcher;

import org.qfox.jestful.client.catcher.Catcher;
import org.qfox.jestful.client.nio.NioClient;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.exception.StatusException;

/**
 * Created by payne on 2017/4/8.
 * Version: 1.0
 */
public interface NioCatcher extends Catcher {

    void nioCatched(NioClient client, Action action, StatusException statusException) throws Exception;

}
