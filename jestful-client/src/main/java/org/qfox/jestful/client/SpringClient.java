package org.qfox.jestful.client;

import org.springframework.beans.factory.FactoryBean;

public class SpringClient extends Client.Builder<SpringClient> implements FactoryBean<Client> {
    private volatile Client client;

    @Override
    public Client getObject() throws Exception {
        return client != null ? client : (client = build());
    }

    @Override
    public Class<?> getObjectType() {
        return Client.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

}
