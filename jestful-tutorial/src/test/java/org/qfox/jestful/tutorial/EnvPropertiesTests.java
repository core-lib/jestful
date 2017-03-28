package org.qfox.jestful.tutorial;

import org.junit.Test;

/**
 * Created by payne on 2017/3/29.
 */
public class EnvPropertiesTests {

    @Test
    public void testENV() throws Exception {
        for (String key : System.getenv().keySet()) {
            System.out.println(key + ":" + System.getenv(key));
        }
    }

    @Test
    public void testProperties() throws Exception {
        for (String name : System.getProperties().stringPropertyNames()) {
            System.out.println(name + ":" + System.getProperty(name));
        }
    }

}
