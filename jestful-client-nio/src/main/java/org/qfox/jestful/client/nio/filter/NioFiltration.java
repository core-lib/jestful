package org.qfox.jestful.client.nio.filter;

import org.qfox.jestful.client.nio.NioClient;
import org.qfox.jestful.core.Action;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by yangchangpei on 17/9/29.
 */
public class NioFiltration {
    private final Iterable<NioFilter> filters;
    private final Iterator<NioFilter> invokeIterator;
    private final Iterator<NioFilter> returnIterator;

    public NioFiltration(Iterable<NioFilter> filters) {
        this.filters = filters;
        this.invokeIterator = filters.iterator();
        this.returnIterator = filters.iterator();
    }

    public Object doInvoke(NioClient client, Action action) throws Exception {
        try {
            return invokeIterator.next().doInvoke(client, action, this);
        } catch (NoSuchElementException e) {
            throw new DuplicateFiltrateException("duplicate call filtration in filters", e, filters);
        }
    }

    public void doReturn(NioClient client, Action action) throws Exception {
        try {
            returnIterator.next().doReturn(client, action, this);
        } catch (NoSuchElementException e) {
            throw new DuplicateFiltrateException("duplicate call filtration in filters", e, filters);
        }
    }

}
