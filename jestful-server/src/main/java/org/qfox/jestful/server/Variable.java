package org.qfox.jestful.server;

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
 * @date 2016年4月1日 下午7:58:58
 *
 * @since 1.0.0
 */
public class Variable {
	private final String name;
	private final int index;
	private final String definition;
	private final String expression;

	public Variable(String name, int index, String definition, String expression) {
		super();
		this.name = name;
		this.index = index;
		this.definition = definition;
		this.expression = expression;
	}

	public String getName() {
		return name;
	}

	public int getIndex() {
		return index;
	}

	public String getDefinition() {
		return definition;
	}

	public String getExpression() {
		return expression;
	}

}
