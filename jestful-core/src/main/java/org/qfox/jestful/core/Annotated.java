package org.qfox.jestful.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

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
 * @date 2016年4月11日 下午3:36:15
 *
 * @since 1.0.0
 */
public abstract class Annotated implements AnnotatedElement {
	protected final Annotation[] annotations;

	protected Annotated(Annotation[] annotations) {
		super();
		this.annotations = annotations;
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
