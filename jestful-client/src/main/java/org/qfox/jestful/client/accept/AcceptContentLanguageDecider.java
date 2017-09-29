package org.qfox.jestful.client.accept;

import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Actor;
import org.qfox.jestful.core.Languages;
import org.qfox.jestful.core.Request;

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
 * @date 2016年5月14日 上午10:12:27
 *
 * @since 1.0.0
 */
public class AcceptContentLanguageDecider implements Actor {

	public Object react(Action action) throws Exception {
		Languages languages = action.getAcceptLanguages();
		if (!languages.isEmpty()) {
			Request request = action.getRequest();
			request.setRequestHeader("Accept-Language", languages.toString());
		}
		return action.execute();
	}

}
