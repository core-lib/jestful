package org.qfox.jestful.cache.evaluation;

/**
 * Created by yangchangpei on 17/9/9.
 */
public interface Evaluator {

    <T> T evaluate(String condition, Object value, Class<T> clazz);

}
