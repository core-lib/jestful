package org.qfox.jestful.server.mapper;

import org.qfox.jestful.commons.MapKit;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Actor;

import java.util.Map;

/**
 * Created by yangchangpei on 17/5/3.
 */
public class QueriesMapper implements Actor {

    @Override
    public Object react(Action action) throws Exception {
        String query = action.getQuery();
        if (query == null || query.length() == 0) return action.execute();

        String charset = action.getQueryEncodeCharset();
        Map<String, String[]> queries = MapKit.valueOf(query, charset);
        action.setQueries(queries);

        return action.execute();
    }

}
