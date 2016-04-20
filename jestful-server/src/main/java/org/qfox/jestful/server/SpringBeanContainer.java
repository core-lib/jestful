package org.qfox.jestful.server;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletContext;

import org.qfox.jestful.commons.collection.Enumerator;
import org.qfox.jestful.core.Bean;
import org.qfox.jestful.core.BeanContainer;
import org.qfox.jestful.core.Destroyable;
import org.qfox.jestful.core.Initialable;
import org.qfox.jestful.core.exception.BeanNonuniqueException;
import org.qfox.jestful.core.exception.BeanUndefinedException;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.web.context.WebApplicationContext;

/**
 * <p>
 * Description: 与Spring集成的bean容器
 * </p>
 * 
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 * 
 * @author Payne 646742615@qq.com
 *
 * @date 2016年4月20日 下午12:22:17
 *
 * @since 1.0.0
 */
public class SpringBeanContainer implements BeanContainer, Initialable, Destroyable {
	private final ListableBeanFactory listableBeanFactory;

	public SpringBeanContainer(ServletContext servletContext) {
		super();
		this.listableBeanFactory = (ListableBeanFactory) servletContext.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
	}

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

	public void initialize(BeanContainer beanContainer) {
		Map<String, Initialable> initialables = find(Initialable.class);
		for (Initialable initialable : initialables.values()) {
			initialable.initialize(this);
		}
	}

	public void destroy() {
		Map<String, Destroyable> destroyables = find(Destroyable.class);
		for (Destroyable destroyable : destroyables.values()) {
			destroyable.destroy();
		}
	}

}
