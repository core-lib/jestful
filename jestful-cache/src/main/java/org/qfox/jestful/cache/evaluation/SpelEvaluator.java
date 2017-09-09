package org.qfox.jestful.cache.evaluation;

import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

/**
 * Created by yangchangpei on 17/9/9.
 */
public class SpelEvaluator implements Evaluator {
    private ExpressionParser parser = new SpelExpressionParser();

    @Override
    public <T> T evaluate(String condition, Object value, Class<T> clazz) {
        return parser.parseExpression(condition).getValue(value, clazz);
    }

    public ExpressionParser getParser() {
        return parser;
    }

    public void setParser(ExpressionParser parser) {
        this.parser = parser;
    }
}
