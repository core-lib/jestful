package org.qfox.jestful.client.catcher;

import org.qfox.jestful.client.Client;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.exception.StatusException;

/**
 * Created by yangchangpei on 17/4/7.
 */
public interface Catcher {

    boolean catchable(StatusException statusException);

    Object catched(Client client, Action action, StatusException statusException) throws Exception;

}
