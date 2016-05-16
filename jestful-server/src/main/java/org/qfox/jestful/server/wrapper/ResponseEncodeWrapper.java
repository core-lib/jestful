package org.qfox.jestful.server.wrapper;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Actor;
import org.qfox.jestful.core.BeanContainer;
import org.qfox.jestful.core.Encoding;
import org.qfox.jestful.core.Encodings;
import org.qfox.jestful.core.Initialable;
import org.qfox.jestful.core.Response;
import org.qfox.jestful.core.codec.ResponseEncoder;
import org.qfox.jestful.core.exception.NoSuchCodecException;
import org.qfox.jestful.server.JestfulServletResponse;

public class ResponseEncodeWrapper implements Actor, Initialable {
	private final Map<Encoding, ResponseEncoder> map = new HashMap<Encoding, ResponseEncoder>();

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
				Encodings expects = action.getContentEncodings();
				Encodings actuals = availables;
				throw new NoSuchCodecException(expects, actuals);
			}
			Encoding encoding = encodings.first();
			ResponseEncoder encoder = map.get(encoding);
			Response source = action.getResponse();
			Response target = new EncodedServletResponse((JestfulServletResponse) source, encoding, encoder);
			target.setResponseHeader("Content-Encoding", encoding.toString(false));
			action.setResponse(target);
		}
		return null;
	}

	public void initialize(BeanContainer beanContainer) {
		Collection<ResponseEncoder> encoders = beanContainer.find(ResponseEncoder.class).values();
		for (ResponseEncoder encoder : encoders) {
			String contentEncoding = encoder.getContentEncoding();
			Encoding encoding = Encoding.valueOf(contentEncoding);
			map.put(encoding, encoder);
		}
	}

}
