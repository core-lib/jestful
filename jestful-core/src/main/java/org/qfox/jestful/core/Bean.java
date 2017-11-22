package org.qfox.jestful.core;

/**
 * <p>
 * Description:
 * </p>
 * <p>
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 *
 * @author Payne 646742615@qq.com
 * @date 2016年4月20日 下午12:03:08
 * @since 1.0.0
 */
public class Bean {
    private final String name;
    private final Object instance;

    public Bean(String name, Object instance) {
        super();
        this.name = name;
        this.instance = instance;
    }

    public String getName() {
        return name;
    }

    public Object getInstance() {
        return instance;
    }

}
