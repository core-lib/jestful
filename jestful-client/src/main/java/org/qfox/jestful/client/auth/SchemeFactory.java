package org.qfox.jestful.client.auth;

import org.qfox.jestful.core.Action;

public interface SchemeFactory {

    Scheme produce(Action action);

}
