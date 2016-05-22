package org.qfox.jestful.core;

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
 * @date 2016年5月15日 上午11:14:44
 *
 * @since 1.0.0
 */
public interface Weighted<T extends Weighted<T>> extends Comparable<T> {

	float getWeight();

}
