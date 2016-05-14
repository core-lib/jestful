package org.qfox.jestful.client.accept;  

import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Actor;

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
 * @date 2016年5月14日 上午10:11:48
 *
 * @since 1.0.0 
 */
public class AcceptContentCharsetDecider implements Actor {

	public Object react(Action action) throws Exception {
		return action.execute();
	}

}
 