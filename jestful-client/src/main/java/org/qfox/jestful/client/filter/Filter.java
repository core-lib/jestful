package org.qfox.jestful.client.filter;

import org.qfox.jestful.client.Client;
import org.qfox.jestful.core.Action;

/**
 * Created by yangchangpei on 17/9/28.
 */
public interface Filter {

    Object filtrate(Client client, Action action, Filtration filtration) throws Exception;

}
