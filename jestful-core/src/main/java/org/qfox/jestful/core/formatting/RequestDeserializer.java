package org.qfox.jestful.core.formatting;

import java.io.IOException;
import java.io.InputStream;

import org.qfox.jestful.commons.MediaType;
import org.qfox.jestful.commons.Multihead;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Parameter;

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
 * @date 2016年4月8日 下午5:21:07
 *
 * @since 1.0.0
 */
public interface RequestDeserializer extends Formatting {

	void deserialize(Action action, MediaType mediaType, InputStream in) throws IOException;

	void deserialize(Action action, Parameter parameter, Multihead multihead, InputStream in) throws IOException;

}
