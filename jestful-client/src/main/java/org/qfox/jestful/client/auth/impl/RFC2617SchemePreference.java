package org.qfox.jestful.client.auth.impl;

import org.qfox.jestful.client.auth.SchemePreference;

/**
 * Created by yangchangpei on 17/10/25.
 */
public class RFC2617SchemePreference extends OrderedSchemePreference implements SchemePreference {

    public RFC2617SchemePreference() {
        super(DigestScheme.NAME, BasicScheme.NAME);
    }

}
