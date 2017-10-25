package org.qfox.jestful.client.auth.impl;

import org.qfox.jestful.client.auth.SchemePreference;

/**
 * Created by yangchangpei on 17/10/25.
 */
public class RFC2671SchemePreference extends OrderedSchemePreference implements SchemePreference {

    public RFC2671SchemePreference() {
        super(DigestScheme.NAME, BasicScheme.NAME);
    }

}
