package org.qfox.jestful.client.auth;

import java.util.Map;

/**
 * Created by Payne on 2017/10/20.
 */
public interface Scheme {

    Map<String, String[]> cope(Challenge challenge);

}
