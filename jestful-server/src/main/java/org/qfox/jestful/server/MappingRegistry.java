package org.qfox.jestful.server;

import java.util.Collection;

import org.qfox.jestful.core.Mapping;
import org.qfox.jestful.core.Resource;
import org.qfox.jestful.core.exception.IllegalConfigException;
import org.qfox.jestful.server.exception.BadMethodStatusException;
import org.qfox.jestful.server.exception.NotFoundStatusException;

/**
 * <p>
 * Description: URI 资源注册器
 * </p>
 * 
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 * 
 * @author Payne 646742615@qq.com
 *
 * @date 2016年3月31日 上午10:09:06
 *
 * @since 1.0.0
 */
public interface MappingRegistry {

	/**
	 * 搜索支持指定匹配指定URI的所有资源
	 * 
	 * @param URI
	 *            请求路径
	 * @return 匹配的资源
	 * @throws NotFoundStatusException
	 *             请求路径不存在
	 * @throws BadMethodStatusException
	 *             请求方法不支持
	 */
	Collection<Mapping> lookup(String URI) throws NotFoundStatusException;

	/**
	 * 搜索支持指定请求方法并且匹配指定URI的资源
	 * 
	 * @param command
	 *            请求方法
	 * @param URI
	 *            请求路径
	 * @return 匹配的资源
	 * @throws NotFoundStatusException
	 *             请求路径不存在
	 * @throws BadMethodStatusException
	 *             请求方法不支持
	 */
	Mapping lookup(String command, String URI) throws NotFoundStatusException, BadMethodStatusException;

	/**
	 * 注册资源控制器
	 * 
	 * @param controller
	 *            资源控制器
	 * @return
	 * @throws IllegalConfigException
	 */
	Resource register(Object controller) throws IllegalConfigException;

	/**
	 * 批量注册资源控制器
	 * 
	 * @param controllers
	 *            资源控制器数组
	 * @return
	 * @throws IllegalConfigException
	 */
	Collection<Resource> register(Object... controllers) throws IllegalConfigException;

	/**
	 * 批量注册资源控制器
	 * 
	 * @param controllers
	 *            资源控制器数组
	 * @return
	 * @throws IllegalConfigException
	 */
	Collection<Resource> register(Collection<Object> controllers) throws IllegalConfigException;

}
