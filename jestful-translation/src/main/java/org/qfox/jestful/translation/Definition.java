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
 * @date 2016年6月21日 下午3:04:16
 *
 * @since 1.0.0
 */
public abstract class Definition<T extends Definition<T>> implements Comparable<T> {
	protected Set<Model> annotations = new TreeSet<Model>();

	protected Definition(Annotation[] annotations) {
		for (Annotation annotation : annotations) {
			this.annotations.add(new Model(annotation));
		}
	}

	public Set<Model> getAnnotations() {
		return annotations;
	}

	public void setAnnotations(Set<Model> annotations) {
		this.annotations = annotations;
	}

}
