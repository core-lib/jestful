package org.qfox.jestful.cache.conversion;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.qfox.jestful.cache.Conversion;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
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
        ExpressionParser parser = new SpelExpressionParser();
        EvaluationContext context = new StandardEvaluationContext(12);
        Boolean value = parser.parseExpression("#this > 10").getValue(context, Boolean.class);
        System.out.println(value);
    }

}