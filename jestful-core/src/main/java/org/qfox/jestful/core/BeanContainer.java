package org.qfox.jestful.core;

import java.lang.annotation.Annotation;
import java.util.Map;

import org.qfox.jestful.commons.collection.Enumerable;
import org.qfox.jestful.core.exception.BeanNonuniqueException;
import org.qfox.jestful.core.exception.BeanUndefinedException;

/**
 * <p>
 * Description: bean 容器
 * </p>
 * 
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 * 
 * @author Payne 646742615@qq.com
 *
 * @date 2016年4月20日 上午11:55:44
 *
 * @since 1.0.0
 */
public interface BeanContainer extends Enumerable<Bean> {

	/**
	 * check whether the bean container contains a bean named of the specified name parameter
	 * 
	 * @param name
	 *            bean name
	 * @return true if contains, false else
	 */
	boolean contains(String name);

	/**
	 * get bean of specified name
	 * 
	 * @param name
	 *            bean name
	 * @return bean named of specified parameter
	 * @throws BeanUndefinedException
	 *             if not contains any bean named of specified parameter
	 */
	Object get(String name) throws BeanUndefinedException;

	/**
	 * get bean of specified name
	 * 
	 * @param name
	 *            bean name
	 * @return bean named of specified parameter
	 * @throws BeanUndefinedException
	 *             if not contains any bean named of specified parameter
	 * @throws ClassCastException
	 *             if bean named name parameter but isn's instance of type parameter
	 */
	<T> T get(String name, Class<T> type) throws BeanUndefinedException, ClassCastException;

	/**
	 * check whether the bean container contains a bean of type specified type parameter
	 * 
	 * @param type
	 *            bean type
	 * @return true if contains, false else
	 */
	boolean contains(Class<?> type);

	/**
	 * get unique bean of specified name
	 * 
	 * @param type
	 *            bean type
	 * @return bean of type specified type parameter
	 * @throws BeanUndefinedException
	 *             if no any bean is instance of specified type parameter
	 * @throws BeanNonuniqueException
	 *             if more than one bean is instance of specified type parameter
	 */
	<T> T get(Class<T> type) throws BeanUndefinedException, BeanNonuniqueException;

	/**
	 * find all beans instance of specified type parameter and use it name as entry key, itself as entry value
	 * 
	 * @param type
	 *            bean type
	 * @return <bean name, bean> map, it may be an empty map when the bean container doesn't contains any bean is
	 *         instance of the specified type parameter
	 */
	<T> Map<String, T> find(Class<T> type);

	/**
	 * find all beans which type is present an annotation of specified annotation type and use it name as entry key,
	 * itself as entry value
	 * 
	 * @param annotationType
	 *            annotation type
	 * @return <bean name, bean> map, it may be an empty map when the bean container doesn't contains any bean's type of
	 *         the specified annotation type parameter
	 */
	Map<String, ?> with(Class<? extends Annotation> annotationType);

}
