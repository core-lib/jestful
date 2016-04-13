package org.qfox.jestful.server.render;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.MapContext;
import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Actor;
import org.qfox.jestful.core.Movement;
import org.qfox.jestful.core.Parameter;
import org.qfox.jestful.core.Result;

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
 * @date 2016年4月13日 上午11:22:26
 *
 * @since 1.0.0
 */
public class RedirectResultRender implements Actor {
	private String regex = "\\$\\{\\s*([^${}]+?)\\s*\\}";
	private int group = 1;
	private JexlEngine jexlEngine = new JexlEngine();

	public Object react(Action action) throws Exception {
		Result result = action.getResult();
		if (result.getMovement() != Movement.FORWARD) {
			return action.execute();
		}
		Object value = action.execute();

		Parameter[] parameters = action.getParameters();
		Object[] arguments = new Object[parameters.length];
		for (int i = 0; i < parameters.length; i++) {
			arguments[i] = parameters[i].getValue();
		}

		JexlContext context = new MapContext();
		context.set("action", action);
		context.set("result", value);
		context.set("arguments", arguments);
		context.set("request", action.getRequest());
		context.set("response", action.getResponse());

		String path = result.getPath();
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(path);
		while (matcher.find()) {
			String expression = matcher.group();
			String variable = matcher.group(group);
			Object evaluation = jexlEngine.createExpression(variable).evaluate(context);
			path = path.replace(expression, String.valueOf(evaluation));
		}

		return value;
	}

	public String getRegex() {
		return regex;
	}

	public void setRegex(String regex) {
		this.regex = regex;
	}

	public int getGroup() {
		return group;
	}

	public void setGroup(int group) {
		this.group = group;
	}

	public JexlEngine getJexlEngine() {
		return jexlEngine;
	}

	public void setJexlEngine(JexlEngine jexlEngine) {
		this.jexlEngine = jexlEngine;
	}

}
