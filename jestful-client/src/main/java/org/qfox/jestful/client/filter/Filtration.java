package org.qfox.jestful.client.filter;

import org.qfox.jestful.client.Client;
import org.qfox.jestful.core.Action;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by yangchangpei on 17/9/29.
 */
public class Filtration {
    private final Iterable<Filter> filters;
    private final Iterator<Filter> invokeIterator;
    private final Iterator<Filter> returnIterator;

    public Filtration(Iterable<Filter> filters) {
        this.filters = filters;
        this.invokeIterator = filters.iterator();
        this.returnIterator = filters.iterator();
    }

    public Object doInvoke(Client client, Action action) throws Exception {
        try {
            return invokeIterator.next().doInvoke(client, action, this);
        } catch (NoSuchElementException e) {
            throw new DuplicateFiltrateException("duplicate call filtration in filters", e, filters);
        }
    }

    public void doReturn(Client client, Action action) throws Exception {
        try {
            returnIterator.next().doReturn(client, action, this);
        } catch (NoSuchElementException e) {
            throw new DuplicateFiltrateException("duplicate call filtration in filters", e, filters);
        }
    }

}
