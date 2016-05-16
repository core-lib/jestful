package org.qfox.jestful.client.accept;

import java.nio.charset.Charset;

import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Actor;
import org.qfox.jestful.core.Charsets;
import org.qfox.jestful.core.Request;
import org.qfox.jestful.core.exception.NoSuchCharsetException;

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
	private final Charsets supports = new Charsets(Charset.availableCharsets().keySet().toArray(new String[0]));

	public Object react(Action action) throws Exception {
		Charsets charsets = action.getAcceptCharsets().clone();
		if (charsets.isEmpty() == false) {
			charsets.retainAll(supports);
			if (charsets.isEmpty()) {
				throw new NoSuchCharsetException(action.getAcceptCharsets().clone(), supports.clone());
			}
			Request request = action.getRequest();
			request.setRequestHeader("Accept-Charset", charsets.toString());
		}
		return action.execute();
	}

}
