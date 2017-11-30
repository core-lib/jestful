package org.qfox.jestful.core;

import org.qfox.jestful.commons.Destructible;
import org.qfox.jestful.commons.Destruction;
import org.qfox.jestful.commons.IOKit;
import org.qfox.jestful.commons.collection.Enumerator;
import org.qfox.jestful.core.exception.BeanNonuniqueException;
import org.qfox.jestful.core.exception.BeanUndefinedException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

/**
 * <p>
 * Description:
 * </p>
 * <p>
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 *
 * @author Payne 646742615@qq.com
 * @date 2016年4月27日 下午9:46:56
 * @since 1.0.0
 */
public class DefaultBeanContainer implements BeanContainer, BeanPostProcessor, BeanFactoryAware {
    private final ConcurrentMap<String, Queue<Object>> beans = new ConcurrentHashMap<String, Queue<Object>>();
    private volatile boolean destroyed;
    private ListableBeanFactory listableBeanFactory;

    public Enumeration<Bean> enumeration() {
        Map<String, Object> map = listableBeanFactory.getBeansOfType(Object.class);
        List<Bean> beans = new ArrayList<Bean>();
        for (Entry<String, Object> entry : map.entrySet()) {
            Bean bean = new Bean(entry.getKey(), entry.getValue());
            beans.add(bean);
        }
        return new Enumerator<Bean>(beans.iterator());
    }

    public boolean contains(String name) {
        return listableBeanFactory.containsBean(name);
    }

    public Object get(String name) throws BeanUndefinedException {
        try {
            return listableBeanFactory.getBean(name);
        } catch (NoSuchBeanDefinitionException e) {
            throw new BeanUndefinedException(name, this);
        }
    }

    public <T> T get(String name, Class<T> type) throws BeanUndefinedException, ClassCastException {
        Object bean = get(name);
        return type.cast(bean);
    }

    public boolean contains(Class<?> type) {
        return !find(type).isEmpty();
    }

    public <T> T get(Class<T> type) throws BeanUndefinedException, BeanNonuniqueException {
        try {
            return listableBeanFactory.getBean(type);
        } catch (NoUniqueBeanDefinitionException e) {
            throw new BeanNonuniqueException(type, this);
        } catch (NoSuchBeanDefinitionException e) {
            throw new BeanUndefinedException(type, this);
        }
    }

    public <T> Map<String, T> find(Class<T> type) {
        return listableBeanFactory.getBeansOfType(type);
    }

    @Override
    public <T> Map<String, T> find(Class<T> type, boolean includeNonSingletons, boolean allowEagerInit) {
        return listableBeanFactory.getBeansOfType(type, includeNonSingletons, allowEagerInit);
    }

    public Map<String, ?> with(Class<? extends Annotation> annotationType) {
        return listableBeanFactory.getBeansWithAnnotation(annotationType);
    }

    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Queue<Object> queue = beans.get(beanName);
        if (queue == null) {
            Queue<Object> old = beans.putIfAbsent(beanName, queue = new ConcurrentLinkedQueue<Object>());
            if (old != null) queue = old;
        }
        queue.offer(bean);
        if (bean instanceof Initialable) {
            Initialable initialable = (Initialable) bean;
            initialable.initialize(this);
        }
        return bean;
    }

    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        if (beanFactory instanceof ConfigurableBeanFactory) {
            ConfigurableBeanFactory configurableBeanFactory = (ConfigurableBeanFactory) beanFactory;
            configurableBeanFactory.addBeanPostProcessor(this);
        }
        this.listableBeanFactory = (ListableBeanFactory) beanFactory;
    }

    @Override
    public synchronized void destroy() {
        if (destroyed) return;
        destroyed = true;
        for (Queue<Object> queue : beans.values()) {
            for (Object bean : queue) {
                if (bean instanceof Destroyable) {
                    IOKit.close(new Destruction((Destructible) bean));
                }
            }
            queue.clear();
        }
        beans.clear();
    }
}
