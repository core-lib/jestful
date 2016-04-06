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
public class Variable implements Comparable<Variable> {
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

	public int compareTo(Variable o) {
		return index > o.index ? 1 : index < o.index ? -1 : 0;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + index;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Variable other = (Variable) obj;
		if (index != other.index)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return definition;
	}

}
