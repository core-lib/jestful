package org.qfox.jestful.server.obtainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Parameter;

/**
 * <p>
 * Description: 额外参数获取器,例如 {@link HttpServletRequest} {@link HttpServletResponse}...
 * </p>
 * 
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 * 
 * @author Payne 646742615@qq.com
 *
 * @date 2016年4月19日 下午3:42:02
 *
 * @since 1.0.0
 */
public interface Obtainer {

	/**
	 * 从{@link Action}获取指定参数{@link Parameter}的值并返回
	 * 
	 * @param action
	 *            action
	 * @param parameter
	 *            参数
	 * @return 参数值,如果该获取器不支持该参数的获取也返回null
	 */
	Object obtain(Action action, Parameter parameter);

}
