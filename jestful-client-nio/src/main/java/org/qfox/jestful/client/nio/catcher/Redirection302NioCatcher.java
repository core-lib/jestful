package org.qfox.jestful.client.nio.catcher;

import org.qfox.jestful.core.exception.StatusException;

/**
 * Created by yangchangpei on 17/4/7.
 */
public class Redirection302NioCatcher extends Redirection301NioCatcher implements NioCatcher {
    @Override
    public boolean catchable(StatusException statusException) {
        return statusException.getStatus() == 302;
    }
}
