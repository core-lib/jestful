/**
 * utils[com.change.utils.reflect]
 * Change
 * 2014年1月12日 下午12:13:47
 */

package org.qfox.jestful.commons.type;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;

/**
 * 泛型数组类型实现
 *
 * @author Change
 * @date 2014年1月12日 下午12:13:47
 * <p>
 * 修订记录<br/>
 * 姓名:Change<br/>
 * 时间:2014年1月12日 下午12:13:47<br/>
 * 内容:初始<br/>
 */
public class GenericArrayTypeImpl implements GenericArrayType {

    private Type genericComponentType;

    public GenericArrayTypeImpl(Type genericComponentType) {
        super();
        this.genericComponentType = genericComponentType;
    }

    public Type getGenericComponentType() {
        return genericComponentType;
    }

    @Override
    public String toString() {
        return genericComponentType + "[]";
    }

}
