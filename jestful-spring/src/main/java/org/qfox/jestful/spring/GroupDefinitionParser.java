package org.qfox.jestful.spring;

import java.util.List;

import org.qfox.jestful.core.Group;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSimpleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

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
 * @date 2016年5月9日 下午5:36:35
 *
 * @since 1.0.0
 */
public class GroupDefinitionParser extends AbstractSimpleBeanDefinitionParser {

	@Override
	protected String getBeanClassName(Element element) {
		return Group.class.getName();
	}

	@Override
	protected void doParse(Element element, ParserContext context, BeanDefinitionBuilder builder) {
		super.doParse(element, context, builder);
		List<?> members = context.getDelegate().parseListElement(element, builder.getBeanDefinition());
		builder.addPropertyValue("members", members);
	}

}
