package org.qfox.jestful.server.path;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Group;
import org.qfox.jestful.core.Parameter;
import org.qfox.jestful.core.Source;

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
 * @date 2016年4月7日 下午8:36:13
 *
 * @since 1.0.0
 */
public class PathArgumentResolver extends Group {

	@Override
	public Object react(Action action) throws Exception {
		String URI = action.getURI();
		Parameter[] parameters = action.getParameters();
		Pattern pattern = action.getPattern();
		Matcher matcher = pattern.matcher(URI);
		matcher.find();
		for (int i = 0; i < parameters.length; i++) {
			Parameter parameter = parameters[i];
			if (parameter.getSource() != Source.PATH) {
				continue;
			}
			int group = parameter.getGroup();
			String value = matcher.group(group);
			parameter.setValue(value);
		}
		return super.react(action);
	}

}
