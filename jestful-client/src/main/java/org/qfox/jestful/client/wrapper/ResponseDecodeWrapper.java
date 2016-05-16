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
import org.qfox.jestful.core.Response;
import org.qfox.jestful.core.codec.ResponseDecoder;
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
 * @date 2016年5月15日 上午10:12:06
 *
 * @since 1.0.0
 */
public class ResponseDecodeWrapper implements Actor, Initialable {
	private final Map<Encoding, ResponseDecoder> map = new HashMap<Encoding, ResponseDecoder>();

	public Object react(Action action) throws Exception {
		if (action.isAcceptEncode()) {
			Encodings encodings = action.getAcceptEncodings().clone();
			Encodings availables = new Encodings(map.keySet());
			if (encodings.isEmpty() == false) {
				encodings.retainAll(availables);
			} else {
				encodings = availables;
			}
			if (encodings.isEmpty()) {
				Encodings expects = action.getAcceptEncodings();
				Encodings actuals = availables;
				throw new NoSuchCodecException(expects, actuals);
			}
			Map<Encoding, ResponseDecoder> decoders = new HashMap<Encoding, ResponseDecoder>();
			for (Encoding encoding : encodings) {
				decoders.put(encoding, map.get(encoding));
			}
			Response source = action.getResponse();
			Response target = new DecodedResponse(source, decoders);
			Request request = action.getRequest();
			request.setRequestHeader("Accept-Encoding", encodings.toString());
			action.setResponse(target);
		}
		return action.execute();
	}

	public void initialize(BeanContainer beanContainer) {
		Collection<ResponseDecoder> decoders = beanContainer.find(ResponseDecoder.class).values();
		for (ResponseDecoder decoder : decoders) {
			String contentEncoding = decoder.getContentEncoding();
			Encoding encoding = Encoding.valueOf(contentEncoding);
			map.put(encoding, decoder);
		}
	}

}
