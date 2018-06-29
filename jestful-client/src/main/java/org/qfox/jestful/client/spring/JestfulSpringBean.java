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
    private Class<T> interfase;
    private Client client;
    private boolean singleton;
    private volatile T object;

    @Override
    public T getObject() throws Exception {
        return singleton ? (object != null ? object : (object = client.create(interfase))) : client.create(interfase);
    }

    @Override
    public Class<?> getObjectType() {
        return interfase;
    }

    @Override
    public boolean isSingleton() {
        return singleton;
    }

    public Class<T> getInterfase() {
        return interfase;
    }

    public void setInterfase(Class<T> interfase) {
        this.interfase = interfase;
    }

    public void setSingleton(boolean singleton) {
        this.singleton = singleton;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
