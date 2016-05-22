package org.qfox.jestful.core.formatting;

import java.io.IOException;
import java.io.OutputStream;

import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Parameter;
import org.qfox.jestful.core.io.MultipartOutputStream;

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
 * @date 2016年4月8日 下午5:08:58
 *
 * @since 1.0.0
 */
public interface RequestSerializer extends Formatting {

	boolean supports(Action action);

	boolean supports(Parameter parameter);

	void serialize(Action action, String charset, OutputStream out) throws IOException;

	void serialize(Action action, Parameter parameter, String charset, MultipartOutputStream out) throws IOException;

}
