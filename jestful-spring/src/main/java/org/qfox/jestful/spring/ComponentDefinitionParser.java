package org.qfox.jestful.spring;

import org.qfox.jestful.core.Component;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSimpleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import java.util.List;

/**
 * <p>
 * Description:
 * </p>
 * <p>
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 *
 * @author Payne 646742615@qq.com
 * @date 2016年5月14日 上午11:23:36
 * @since 1.0.0
 */
public class ComponentDefinitionParser extends AbstractSimpleBeanDefinitionParser {

    @Override
    protected String getBeanClassName(Element element) {
        return Component.class.getName();
    }

    @Override
    protected void doParse(Element element, ParserContext context, BeanDefinitionBuilder builder) {
        super.doParse(element, context, builder);
        List<?> members = context.getDelegate().parseListElement(element, builder.getBeanDefinition());
        builder.addPropertyValue("members", members);
    }

}
