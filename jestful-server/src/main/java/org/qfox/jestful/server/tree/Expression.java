package org.qfox.jestful.server.tree;

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
 * @date 2016年3月31日 下午2:53:40
 *
 * @since 1.0.0
 */
public interface Expression<T extends Expression<T>> extends Comparable<T> {

	boolean match(String value);

	String pattern();

	int hashCode();

	boolean equals(Object obj);

}
