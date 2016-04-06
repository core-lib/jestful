package org.qfox.jestful.server;

import org.qfox.jestful.server.exception.BadMethodStatusException;
import org.qfox.jestful.server.exception.IllegalConfigException;
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
	 * 搜索支持指定请求方法并且匹配指定URI的资源
	 * 
	 * @param method
	 *            请求方法
	 * @param uri
	 *            请求路径
	 * @return 匹配的资源
	 * @throws NotFoundStatusException
	 *             请求路径不存在
	 * @throws BadMethodStatusException
	 *             请求方法不支持
	 */
	Mapping lookup(String method, String uri) throws NotFoundStatusException, BadMethodStatusException;

	/**
	 * 注册资源控制器
	 * 
	 * @param controller
	 *            资源控制器
	 * @return
	 * @throws IllegalConfigException
	 */
	Resource register(Object controller) throws IllegalConfigException;

}
