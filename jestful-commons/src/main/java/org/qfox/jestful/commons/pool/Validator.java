package org.qfox.jestful.commons.pool;

public interface Validator<Item> {

    boolean validate(Item item);

    /**
     * calculate the item's timeout delay in milliseconds
     *
     * @param item the item
     * @return the timeout delay in milliseconds of the item
     */
    long timeout(Item item);

}
