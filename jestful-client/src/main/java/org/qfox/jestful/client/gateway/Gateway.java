package org.qfox.jestful.client.gateway;

import java.io.IOException;
import java.net.Proxy;

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
 * @date 2016年6月13日 下午3:25:46
 *
 * @since 1.0.0
 */
public interface Gateway {

	boolean isProxy();

	Proxy toProxy();

	void onConnected(Action action) throws IOException;
	
	Gateway NULL = new Gateway() {
		
		public Proxy toProxy() {
			return null;
		}
		
		public void onConnected(Action action) throws IOException {
			
		}
		
		public boolean isProxy() {
			return false;
		}
		
	};
	
}
