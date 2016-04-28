package org.qfox.jestful.client;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.qfox.jestful.commons.collection.Enumerator;
import org.qfox.jestful.core.Bean;
import org.qfox.jestful.core.BeanContainer;
import org.qfox.jestful.core.Initialable;
import org.qfox.jestful.core.exception.BeanNonuniqueException;
import org.qfox.jestful.core.exception.BeanUndefinedException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

/**
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 * 
 * @author Payne 646742615@qq.com
 *
 * @date 2016年4月27日 下午9:46:56
 *
 * @since 1.0.0
 */
public class DefaultBeanContainer implements BeanContainer, BeanPostProcessor, BeanFactoryAware {
	private ListableBeanFactory listableBeanFactory;

	public Enumeration<Bean> enumeration() {
		Map<String, Object> map = listableBeanFactory.getBeansOfType(Object.class);
		List<Bean> beans = new ArrayList<Bean>();
		for (Entry<String, Object> entry : map.entrySet()) {
			Bean bean = new Bean(entry.getKey(), entry.getValue());
			beans.add(bean);
		}
		Enumerator<Bean> enumerator = new Enumerator<Bean>(beans.iterator());
		return enumerator;
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
		return find(type).isEmpty() == false;
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

	public Map<String, ?> with(Class<? extends Annotation> annotationType) {
		return listableBeanFactory.getBeansWithAnnotation(annotationType);
	}

	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
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

}
