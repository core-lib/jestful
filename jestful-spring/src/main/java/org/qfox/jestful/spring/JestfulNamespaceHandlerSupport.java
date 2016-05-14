package org.qfox.jestful.spring;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

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
 * @date 2016年5月9日 下午5:35:05
 *
 * @since 1.0.0
 */
public class JestfulNamespaceHandlerSupport extends NamespaceHandlerSupport {

	public void init() {
		registerBeanDefinitionParser("group", new GroupDefinitionParser());
		registerBeanDefinitionParser("component", new ComponentDefinitionParser());
	}

}
