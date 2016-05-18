package org.qfox.jestful.client.wrapper;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Actor;
import org.qfox.jestful.core.BeanContainer;
import org.qfox.jestful.core.Encoding;
import org.qfox.jestful.core.Encodings;
import org.qfox.jestful.core.Initialable;
import org.qfox.jestful.core.Request;
import org.qfox.jestful.core.codec.RequestEncoder;
import org.qfox.jestful.core.exception.NoSuchCodecException;

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
 * @date 2016年5月15日 上午10:11:37
 *
 * @since 1.0.0
 */
public class RequestEncodeWrapper implements Actor, Initialable {
	private final Map<Encoding, RequestEncoder> map = new HashMap<Encoding, RequestEncoder>();

	public Object react(Action action) throws Exception {
		if (action.isAllowEncode()) {
			Encodings encodings = action.getContentEncodings().clone();
			Encodings availables = new Encodings(map.keySet());
			if (encodings.isEmpty() == false) {
				encodings.retainAll(availables);
			} else {
				encodings = availables;
			}
			if (encodings.isEmpty()) {
				Encodings expects = action.getContentEncodings().clone();
				Encodings actuals = availables;
				throw new NoSuchCodecException(expects, actuals);
			}
			Encoding encoding = encodings.first();
			RequestEncoder encoder = map.get(encoding);
			Request source = action.getRequest();
			Request target = new EncodedRequest(source, encoding, encoder);
			target.setRequestHeader("Content-Encoding", encoding.toString(false));
			action.setRequest(target);
		}
		return action.execute();
	}

	public void initialize(BeanContainer beanContainer) {
		Collection<RequestEncoder> encoders = beanContainer.find(RequestEncoder.class).values();
		for (RequestEncoder encoder : encoders) {
			String contentEncoding = encoder.getContentEncoding();
			Encoding encoding = Encoding.valueOf(contentEncoding);
			map.put(encoding, encoder);
		}
	}

}
