package org.qfox.jestful.client.connection;

import java.io.IOException;

import org.qfox.jestful.client.Client;
import org.qfox.jestful.client.gateway.Gateway;
import org.qfox.jestful.core.Action;

public interface Connector {

	boolean supports(Action action);

	Connection connect(Action action, Gateway gateway, Client client) throws IOException;

}
