package org.qfox.jestful.core.formatting;

import java.io.IOException;
import java.io.OutputStream;

import org.qfox.jestful.commons.MediaType;
import org.qfox.jestful.core.Action;

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
public interface ResponseSerializer extends Formatting {

	void serialize(Action action, MediaType mediaType, String charset, OutputStream out) throws IOException;

}
