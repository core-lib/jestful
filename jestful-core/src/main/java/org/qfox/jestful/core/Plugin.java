package org.qfox.jestful.core;

import java.util.Map;

import org.qfox.jestful.core.exception.PluginConfigException;

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
 * @date 2016年5月14日 上午10:57:57
 *
 * @since 1.0.0
 */
public interface Plugin extends Actor {

	void config(Map<String, String> arguments) throws PluginConfigException;

}
