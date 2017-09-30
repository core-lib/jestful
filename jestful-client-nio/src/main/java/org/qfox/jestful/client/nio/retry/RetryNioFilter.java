package org.qfox.jestful.client.nio.retry;

import org.qfox.jestful.client.Client;
import org.qfox.jestful.client.PluginAction;
import org.qfox.jestful.client.nio.NioClient;
import org.qfox.jestful.client.nio.filter.NioFilter;
import org.qfox.jestful.client.nio.filter.NioFiltration;
import org.qfox.jestful.core.Action;

/**
 * Created by yangchangpei on 17/9/29.
 */
public class RetryNioFilter implements NioFilter {
    private final int maxTimes;
    private final RetryStrategy retryStrategy;

    public RetryNioFilter() {
        this(3, RetryStrategy.ON_EXCEPTION);
    }

    public RetryNioFilter(int maxTimes, RetryStrategy retryStrategy) {
        this.maxTimes = maxTimes;
        this.retryStrategy = retryStrategy;
    }

    @Override
    public Object doInvoke(NioClient client, Action action, NioFiltration filtration) throws Exception {
        return filtration.doInvoke(client, action);
    }

    @Override
    public void doReturn(NioClient client, Action action, NioFiltration filtration) throws Exception {
        if (retryStrategy.matches(action)) {
            Integer times = (Integer) action.getExtra().get(this.getClass());

            if (times != null && times >= maxTimes) {
                filtration.doReturn(client, action);
                return;
            }

            Client.Invoker<?> invoker = client.invoker()
                    .setProtocol(action.getProtocol())
                    .setHost(action.getHost())
                    .setPort(action.getPort())
                    .setRoute(action.getRoute())
                    .setResource(action.getResource())
                    .setMapping(action.getMapping())
                    .setExtra(this.getClass(), times != null ? times + 1 : 1);

            if (action instanceof PluginAction) {
                PluginAction pa = (PluginAction) action;
                invoker.setForePlugins(pa.getForePlugins());
                invoker.setBackPlugins(pa.getBackPlugins());
            }

            invoker.invoke(action.getParameters().values());
        } else {
            filtration.doReturn(client, action);
        }
    }
}
