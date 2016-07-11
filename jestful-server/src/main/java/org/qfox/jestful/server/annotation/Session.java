package org.qfox.jestful.server.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.qfox.jestful.core.Position;
import org.qfox.jestful.core.annotation.Variable;

/**
 * <p>
 * Description: 会话数据, 通过{@link Session#value()}作为会话键值对的key从当前的会话中获取到指定的对象并且作为方法的调用参数
 * </p>
 * 
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 * 
 * @author Payne 646742615@qq.com
 *
 * @date 2016年7月11日 下午4:01:47
 *
 * @since 1.0.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.PARAMETER })
@Variable(position = Position.SESSION)
public @interface Session {

	/**
	 * 会话键值对的key
	 * 
	 * @return
	 */
	String value();

}
