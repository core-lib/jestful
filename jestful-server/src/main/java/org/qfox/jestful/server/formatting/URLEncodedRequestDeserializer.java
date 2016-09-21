package org.qfox.jestful.server.formatting;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.qfox.jestful.commons.Utils;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.BeanContainer;
import org.qfox.jestful.core.Initialable;
import org.qfox.jestful.core.MediaType;
import org.qfox.jestful.core.Multihead;
import org.qfox.jestful.core.Parameter;
import org.qfox.jestful.core.Position;
import org.qfox.jestful.core.exception.JestfulIOException;
import org.qfox.jestful.core.formatting.RequestDeserializer;
import org.qfox.jestful.core.io.IOUtils;
import org.qfox.jestful.server.converter.ConversionException;
import org.qfox.jestful.server.converter.ConversionProvider;
import org.qfox.jestful.server.converter.IncompatibleConversionException;

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
 * @date 2016年4月8日 下午5:23:18
 *
 * @since 1.0.0
 */
public class URLEncodedRequestDeserializer implements RequestDeserializer, Initialable {
	private ConversionProvider urlConversionProvider;

	public String getContentType() {
		return "application/x-www-form-urlencoded";
	}

	public void deserialize(Action action, MediaType mediaType, String charset, InputStream in) throws IOException {
		InputStreamReader isr = null;
		BufferedReader br = null;
		try {
			Map<String, String[]> map = new HashMap<String, String[]>();
			isr = new InputStreamReader(in);
			br = new BufferedReader(isr);
			String line = null;
			while ((line = br.readLine()) != null) {
				String[] pairs = line.split("&+");
				for (String pair : pairs) {
					String[] keyvalue = pair.split("=+");
					String key = URLDecoder.decode(keyvalue[0], charset);
					String value = keyvalue.length > 1 ? URLDecoder.decode(keyvalue[1], charset) : "";
					if (map.containsKey(key) == false) {
						map.put(key, new String[0]);
					}
					String[] values = map.get(key);
					values = Utils.copyOf(values, values.length + 1);
					values[values.length - 1] = value;
					map.put(key, values);
				}
			}
			List<Parameter> parameters = action.getParameters().all(Position.BODY);
			for (Parameter parameter : parameters) {
				if (parameter.getValue() != null) {
					continue;
				}
				try {
					Object value = urlConversionProvider.convert(parameter.getName(), parameter.getType(), map);
					parameter.setValue(value);
				} catch (IncompatibleConversionException e) {
					throw new JestfulIOException(e);
				} catch (ConversionException e) {
					continue;
				}
			}
		} finally {
			IOUtils.close(br);
			IOUtils.close(isr);
		}
	}

	public void deserialize(Action action, Parameter parameter, Multihead multihead, String charset, InputStream in) throws IOException {
		throw new UnsupportedOperationException();
	}

	public void initialize(BeanContainer beanContainer) {
		urlConversionProvider = beanContainer.get(ConversionProvider.class);
	}

	public ConversionProvider getUrlConversionProvider() {
		return urlConversionProvider;
	}

	public void setUrlConversionProvider(ConversionProvider urlConversionProvider) {
		this.urlConversionProvider = urlConversionProvider;
	}

}
