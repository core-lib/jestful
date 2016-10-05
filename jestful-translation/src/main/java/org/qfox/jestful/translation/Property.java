package org.qfox.jestful.translation;

import java.beans.PropertyDescriptor;

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
 * @date 2016年6月21日 下午3:05:23
 *
 * @since 1.0.0
 */
public class Property extends Definition<Property> {
	private Type type;
	private String name;

	public Property(PropertyDescriptor descriptor) {
		super(descriptor.getReadMethod().getAnnotations());
	}

	public int compareTo(Property o) {
		return name.compareTo(o.name);
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
