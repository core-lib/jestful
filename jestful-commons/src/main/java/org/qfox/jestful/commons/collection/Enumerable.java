package org.qfox.jestful.commons.collection;

import java.util.Enumeration;

/**
 * <p>
 * Description: 可枚举的
 * </p>
 * <p>
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 *
 * @author Payne 646742615@qq.com
 * @date 2016年4月20日 下午12:13:48
 * @since 1.0.0
 */
public interface Enumerable<T> {

    /**
     * 枚举器
     *
     * @return 枚举器
     */
    Enumeration<T> enumeration();

}
