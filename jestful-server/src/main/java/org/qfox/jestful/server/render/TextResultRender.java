package org.qfox.jestful.server.render;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Actor;
import org.qfox.jestful.core.Response;
import org.qfox.jestful.core.Result;
import org.qfox.jestful.core.io.IOUtils;

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
 * @date 2016年6月8日 下午12:30:55
 *
 * @since 1.0.0
 */
public class TextResultRender implements Actor {
	private final Pattern pattern = Pattern.compile("@(\\(([^()]+?)\\))?\\:(.*)");

	public Object react(Action action) throws Exception {
		Object value = action.execute();
		Result result = action.getResult();
		if (result.isRendered()) {
			return value;
		}
		if (value instanceof String == false) {
			return value;
		}
		String text = (String) value;
		if (text.matches(pattern.pattern())) {
			Matcher matcher = pattern.matcher(text);
			matcher.find();
			String type = matcher.group(2) != null ? matcher.group(2) : "text/plain";
			String content = matcher.group(3) != null ? matcher.group(3) : "";
			OutputStreamWriter osw = null;
			try {
				Response response = action.getResponse();
				String charset = response.getResponseHeader("Content-Charset");
				response.setContentType(type + "; charset=" + charset);
				OutputStream out = response.getResponseOutputStream();
				osw = new OutputStreamWriter(out, charset);
				osw.write(content);
				osw.flush();
			} catch (Exception e) {
				throw e;
			} finally {
				result.setRendered(true);
				IOUtils.close(osw);
			}
		}
		return value;
	}

}
