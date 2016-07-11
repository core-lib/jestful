package org.qfox.jestful.translation;

import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.TreeSet;

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
 * @date 2016年6月21日 下午3:05:17
 *
 * @since 1.0.0
 */
public class Model extends Definition<Model> {
	private Type type;
	private Set<Property> properties = new TreeSet<Property>();

	public Model() {
		super(new Annotation[0]);
	}

	public Model(Class<?> clazz) {
		super(clazz.getAnnotations());
	}

	public Model(Annotation annotation) {
		super(new Annotation[0]);
		String directory = annotation.annotationType().getPackage().getName();
		String name = annotation.annotationType().getSimpleName();
		this.type = new Type(directory, name);
	}

	public int compareTo(Model o) {
		return type.compareTo(o.type);
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Set<Property> getProperties() {
		return properties;
	}

	public void setProperties(Set<Property> properties) {
		this.properties = properties;
	}

}
