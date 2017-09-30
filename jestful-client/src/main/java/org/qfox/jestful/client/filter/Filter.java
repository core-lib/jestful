package org.qfox.jestful.client.filter;

import org.qfox.jestful.client.Client;
import org.qfox.jestful.core.Action;

/**
 * Created by yangchangpei on 17/9/29.
 */
public interface Filter {

    Object doInvoke(Client client, Action action, Filtration filtration) throws Exception;

    void doReturn(Client client, Action action, Filtration filtration) throws Exception;

}
