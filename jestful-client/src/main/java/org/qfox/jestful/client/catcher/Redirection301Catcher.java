package org.qfox.jestful.client.catcher;

import org.qfox.jestful.client.Client;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.exception.StatusException;

/**
 * Created by yangchangpei on 17/4/7.
 */
public class Redirection301Catcher implements Catcher {

    @Override
    public boolean catchable(StatusException statusException) {
        return statusException.getStatus() == 301;
    }

    @Override
    public Object catched(Client client, Action action, StatusException statusException) throws Exception {

        return null;
    }

}
