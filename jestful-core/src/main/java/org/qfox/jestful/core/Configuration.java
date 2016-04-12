package org.qfox.jestful.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Description: 框架配置
 * </p>
 * 
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 * 
 * @author Payne 646742615@qq.com
 *
 * @date 2016年4月11日 下午3:36:15
 *
 * @since 1.0.0
 */
public abstract class Configuration implements AnnotatedElement {
	protected final Annotation[] annotations;

	protected Configuration(Annotation[] annotations) {
		super();
		this.annotations = annotations;
	}
	
	public Annotation getAnnotationWith(Class<? extends Annotation> annotationType) {
		for (Annotation annotation : annotations) {
			if (annotation.annotationType().isAnnotationPresent(annotationType)) {
				return annotation;
			}
		}
		return null;
	}

	public Annotation[] getAnnotationsWith(Class<? extends Annotation> annotationType) {
		List<Annotation> list = new ArrayList<Annotation>();
		for (Annotation annotation : annotations) {
			if (annotation.annotationType().isAnnotationPresent(annotationType)) {
				list.add(annotation);
			}
		}
		return list.toArray(new Annotation[list.size()]);
	}

	public boolean isAnnotationPresent(Class<? extends Annotation> annotationType) {
		return getAnnotation(annotationType) != null;
	}

	public <T extends Annotation> T getAnnotation(Class<T> annotationType) {
		for (Annotation annotation : annotations) {
			if (annotation.annotationType() == annotationType) {
				return annotationType.cast(annotation);
			}
		}
		return null;
	}

	public Annotation[] getAnnotations() {
		return annotations;
	}

	public Annotation[] getDeclaredAnnotations() {
		return annotations;
	}

}
