package org.qfox.jestful.client.aio.catcher;

import org.qfox.jestful.core.exception.StatusException;

/**
 * Created by yangchangpei on 17/4/7.
 */
public class Redirection303AioCatcher extends Redirection301AioCatcher implements AioCatcher {
    @Override
    public boolean catchable(StatusException statusException) {
        return statusException.getStatus() == 303;
    }
}
