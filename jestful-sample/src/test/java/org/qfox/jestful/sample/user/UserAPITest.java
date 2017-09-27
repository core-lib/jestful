package org.qfox.jestful.sample.user;

import org.junit.Test;
import org.qfox.jestful.commons.Base64;

/**
 * Created by yangchangpei on 17/9/27.
 */
public class UserAPITest {

    @Test
    public void user() throws Exception {
        String user = UserAPI.INSTANCE.user("Basic " + Base64.encode("core-lib:wan20100101"));
        System.out.println(user);
    }

}