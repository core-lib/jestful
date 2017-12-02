package org.qfox.jestful.commons.pool;

public interface Validator<Item> {

    boolean validate(Item item);

    long timeout(Item item);

}
