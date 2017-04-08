package org.qfox.jestful.client.catcher;

import org.qfox.jestful.core.exception.StatusException;

/**
 * Created by yangchangpei on 17/4/7.
 */
public class Redirection303Catcher extends Redirection301Catcher implements Catcher {
    @Override
    public boolean catchable(StatusException statusException) {
        return statusException.getStatus() == 303;
    }
}
