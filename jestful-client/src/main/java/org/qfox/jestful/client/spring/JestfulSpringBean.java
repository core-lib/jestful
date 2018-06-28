package org.qfox.jestful.client.spring;

import org.qfox.jestful.client.Client;
import org.springframework.beans.factory.FactoryBean;

/**
 * Jestful Spring Bean
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-06-28 12:13
 **/
public class JestfulSpringBean<T> implements FactoryBean<T> {
    private Client client;
    private Class<T> interfase;

    @Override
    public T getObject() throws Exception {
        return client.create(interfase);
    }

    @Override
    public Class<?> getObjectType() {
        return interfase;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Class<T> getInterfase() {
        return interfase;
    }

    public void setInterfase(Class<T> interfase) {
        this.interfase = interfase;
    }
}
