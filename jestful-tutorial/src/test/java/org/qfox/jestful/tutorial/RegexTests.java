package org.qfox.jestful.tutorial;

import org.junit.Test;

import java.net.URL;

/**
 * Created by payne on 2017/3/5.
 */
public class RegexTests {

    @Test
    public void testTrimLastChar() {
        System.out.println("/sdfds///sdfds//".replaceAll("\\/+$", ""));
    }

    @Test
    public void testURLToURI() throws Exception {
        URL url = new URL("http://kaiserin.qfoxtech.com/official/appID/message.res?signature=signature&nonce=12321");
        System.out.println(url.getFile());
    }

}
