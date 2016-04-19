package org.qfox.jestful.server.resolver;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.qfox.jestful.core.Action;
import org.qfox.jestful.core.Actor;
import org.qfox.jestful.core.Parameter;
import org.qfox.jestful.core.annotation.Variable;
import org.qfox.jestful.server.obtainer.Obtainer;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

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
 * @date 2016年4月19日 下午3:34:24
 *
 * @since 1.0.0
 */
public class ExtraParameterResolver implements Actor, ApplicationContextAware {
	private final List<Obtainer> obtainers = new ArrayList<Obtainer>();

	public Object react(Action action) throws Exception {
		Parameter[] parameters = action.getParameters();
		for (Parameter parameter : parameters) {
			Annotation variable = parameter.getAnnotationWith(Variable.class);
			if (variable != null) {
				continue;
			}
			Object value = null;
			Iterator<Obtainer> iterator = obtainers.iterator();
			while (value == null && iterator.hasNext()) {
				value = iterator.next().obtain(action, parameter);
			}
			parameter.setValue(value);
		}
		return action.execute();
	}

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		Map<String, Obtainer> map = applicationContext.getBeansOfType(Obtainer.class);
		obtainers.addAll(map.values());
	}

}
