package org.qfox.jestful.client;  

import org.qfox.jestful.core.annotation.GET;
import org.qfox.jestful.core.annotation.Jestful;

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
 * @date 2016年4月27日 下午7:07:05
 *
 * @since 1.0.0 
 */
@Jestful("/product")
public interface ProductController {
	
	@GET("/index.res")
	public String index();

}
 