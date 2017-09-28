package org.qfox.jestful.client.filter;

import org.qfox.jestful.client.Client;
import org.qfox.jestful.core.Action;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by yangchangpei on 17/9/28.
 */
public class Filtration {
    private final List<Filter> filters;
    private final AtomicInteger index;

    public Filtration(List<Filter> filters) {
        this.filters = filters;
        this.index = new AtomicInteger(0);
    }

    public Object filtrate(Client client, Action action) throws Exception {
        int i = index.getAndIncrement();
        if (filters.size() > i) {
            return filters.get(i).filtrate(client, action, this);
        } else {
            throw new IllegalStateException("illegal call of filter chain");
        }
    }

}
