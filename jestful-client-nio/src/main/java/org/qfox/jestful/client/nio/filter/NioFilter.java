package org.qfox.jestful.client.nio.filter;

import org.qfox.jestful.client.nio.NioClient;
import org.qfox.jestful.core.Action;

/**
 * Created by yangchangpei on 17/9/29.
 */
public interface NioFilter {

    Object doInvoke(NioClient client, Action action, NioFiltration filtration) throws Exception;

    void doReturn(NioClient client, Action action, NioFiltration filtration) throws Exception;

}
