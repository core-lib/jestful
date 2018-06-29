package org.qfox.jestful.client.aio;

import org.qfox.jestful.client.Client;
import org.springframework.beans.factory.FactoryBean;

public class SpringAioClient extends AioClient.AioBuilder<SpringAioClient> implements FactoryBean<Client> {
    private volatile AioClient aioClient;

    @Override
    public Client getObject() throws Exception {
        return aioClient != null ? aioClient : (aioClient = build());
    }

    @Override
    public Class<?> getObjectType() {
        return AioClient.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

}
