package org.qfox.jestful.client;

import org.springframework.beans.factory.FactoryBean;

/**
 * Jestful Spring Bean
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-06-28 12:13
 **/
public class SpringClientBean<T> implements FactoryBean<T> {
    private Class<T> type;
    private Client client;
    private boolean singleton;
    private volatile T object;

    @Override
    public T getObject() throws Exception {
        return singleton ? (object != null ? object : (object = client.create(type))) : client.create(type);
    }

    @Override
    public Class<?> getObjectType() {
        return type;
    }

    @Override
    public boolean isSingleton() {
        return singleton;
    }

    public Class<T> getType() {
        return type;
    }

    public void setType(Class<T> type) {
        this.type = type;
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
