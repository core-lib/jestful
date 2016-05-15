package org.qfox.jestful.core.codec;

import java.io.Closeable;
import java.io.IOException;

import org.qfox.jestful.core.Encoding;

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
 * @date 2016年5月15日 上午9:51:38
 *
 * @since 1.0.0
 */
public interface Codec<T extends Closeable> {

	String getContentEncoding();

	T wrap(T source, Encoding encoding) throws IOException;

}
