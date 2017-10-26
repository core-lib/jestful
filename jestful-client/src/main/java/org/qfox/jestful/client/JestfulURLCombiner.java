package org.qfox.jestful.client;

import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Actor;

public class JestfulURLCombiner implements Actor {

    public Object react(Action action) throws Exception {
        String protocol = action.getProtocol();
        String host = action.getHostname();
        Integer port = action.getPort();
        String route = action.getRoute();
        String uri = action.getURI();
        String query = action.getQuery();
        String url = protocol + "://" + host + (port != null ? ":" + port : "") + (route != null ? route : "") + (uri != null ? uri : "") + (query != null ? "?" + query : "");
        action.setURL(url);
        return action.execute();
    }

}
