package org.qfox.jestful.core;

import java.io.IOException;

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
 * @date 2016年4月12日 下午8:55:03
 *
 * @since 1.0.0
 */
public interface Response extends Message {

	Status getStatus() throws IOException;

	void setStatus(Status status) throws IOException;

}
