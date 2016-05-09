package org.qfox.jestful.core.inspector;

import org.qfox.jestful.core.Mapping;
import org.qfox.jestful.core.Parameter;
import org.qfox.jestful.core.Resource;
import org.qfox.jestful.core.exception.IllegalConfigException;

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
 * @date 2016年5月9日 上午11:54:25
 *
 * @since 1.0.0
 */
public interface Inspector {

	void inspect(Resource resource) throws IllegalConfigException;

	void inspect(Resource resource, Mapping mapping) throws IllegalConfigException;

	void inspect(Resource resource, Mapping mapping, Parameter parameter) throws IllegalConfigException;

}
