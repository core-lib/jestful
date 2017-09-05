package org.qfox.jestful.cache.conversion;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.qfox.jestful.cache.Conversion;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:jestful/*.xml"})
public class SimpleConversionTest {

    @Resource
    private Conversion conversion;

    @Test
    public void convert() throws Exception {
    }

}