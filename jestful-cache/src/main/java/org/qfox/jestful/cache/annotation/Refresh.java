package org.qfox.jestful.cache.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * Description: 强制刷新/忽略缓存, 该注解可以被用在三个地方:<br/>
 * 1. 用于接口上则表明所有的方法都忽略缓存数据, <br/>
 * 2. 用于方法上则表明该方法任何时候都忽略缓存数据, <br/>
 * 3. 用于参数上而且参数类型必须是{@link boolean}类型, 当该参数值是 {@code true}时才忽略数据, 否则不忽略<br/>
 * 需要注意的是三种标注位置的优先级依次递增, 即当方法上标注了该注解而且其中一个{@link boolean} 类型的参数也标注了该注解那么当参数值为false的时候,不忽略缓存数据
 * </p>
 * 
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 * 
 * @author Payne 646742615@qq.com
 *
 * @date 2016年5月31日 下午8:36:21
 *
 * @since 1.0.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD, ElementType.PARAMETER })
public @interface Refresh {

}
