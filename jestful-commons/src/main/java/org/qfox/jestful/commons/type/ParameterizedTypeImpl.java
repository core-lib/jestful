/**
 * utils[com.change.utils.reflect]
 * Change
 * 2014年1月12日 下午12:14:18
 */

package org.qfox.jestful.commons.type;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 泛型类型
 *
 * @author Change
 * @date 2014年1月12日 下午12:14:18
 * <p>
 * 修订记录<br/>
 * 姓名:Change<br/>
 * 时间:2014年1月12日 下午12:14:18<br/>
 * 内容:初始<br/>
 */
public class ParameterizedTypeImpl implements ParameterizedType {

    private Type rawType;

    private Type ownerType;

    private Type[] actualTypeArguments;

    public ParameterizedTypeImpl(Type rawType, Type ownerType, Type[] actualTypeArguments) {
        super();
        this.rawType = rawType;
        this.ownerType = ownerType;
        this.actualTypeArguments = actualTypeArguments;
    }

    public Type getRawType() {
        return rawType;
    }

    public Type getOwnerType() {
        return ownerType;
    }

    public Type[] getActualTypeArguments() {
        return actualTypeArguments;
    }

    @Override
    public String toString() {
        String arguments = "";
        for (int i = 0; i < actualTypeArguments.length; i++) {
            arguments += actualTypeArguments[i];
            if (i < actualTypeArguments.length - 1) {
                arguments += ",";
            }
        }
        return rawType + "<" + arguments + ">";
    }

}
