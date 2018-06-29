package org.qfox.jestful.client.nio;

import org.qfox.jestful.client.Client;
import org.springframework.beans.factory.FactoryBean;

public class SpringNioClient extends NioClient.NioBuilder<SpringNioClient> implements FactoryBean<Client> {
    private volatile NioClient nioClient;

    @Override
    public Client getObject() throws Exception {
        return nioClient != null ? nioClient : (nioClient = build());
    }

    @Override
    public Class<?> getObjectType() {
        return NioClient.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

}
