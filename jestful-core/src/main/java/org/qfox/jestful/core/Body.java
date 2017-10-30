package org.qfox.jestful.core;

import java.lang.reflect.Type;

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
 * @date 2016年5月9日 下午12:16:02
 * @since 1.0.0
 */
public class Body {
    private Type type;
    private Object value;

    public Body(Type type) {
        super();
        this.type = type;
    }

    public Body reset() {
        value = null;
        return this;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

}
