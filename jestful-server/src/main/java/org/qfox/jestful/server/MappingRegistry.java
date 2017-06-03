package org.qfox.jestful.server;

import org.qfox.jestful.core.Mapping;
import org.qfox.jestful.core.Resource;
import org.qfox.jestful.core.exception.IllegalConfigException;
import org.qfox.jestful.server.exception.BadMethodStatusException;
import org.qfox.jestful.server.exception.ConflictStatusException;
import org.qfox.jestful.server.exception.NotFoundStatusException;

import java.util.Collection;
import java.util.Comparator;
import java.util.Enumeration;

/**
 * <p>
 * Description: URI 资源注册器
 * </p>
 * <p>
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 *
 * @author Payne 646742615@qq.com
 * @date 2016年3月31日 上午10:09:06
 * @since 1.0.0
 */
public interface MappingRegistry {

    /**
     * 搜索支持指定请求方法并且匹配指定URI的资源
     *
     * @param URI 请求路径
     * @return 匹配的资源集合
     * @throws NotFoundStatusException  请求路径不存在
     * @throws BadMethodStatusException 请求方法不支持
     */
    Collection<Mapping> lookup(String URI) throws NotFoundStatusException, BadMethodStatusException;

    /**
     * 搜索支持指定请求方法并且匹配指定URI的资源
     *
     * @param method 请求方法
     * @param URI    请求路径
     * @return 匹配的资源集合
     * @throws NotFoundStatusException  请求路径不存在
     * @throws BadMethodStatusException 请求方法不支持
     */
    Collection<Mapping> lookup(String method, String URI) throws NotFoundStatusException, BadMethodStatusException;

    /**
     * 搜索支持指定请求方法并且匹配指定URI的资源
     *
     * @param method     请求方法
     * @param URI        请求路径
     * @param accept     请求内容协商的内容类型和版本信息
     * @param comparator 版本对比器
     * @return 匹配的资源
     * @throws NotFoundStatusException  请求路径不存在
     * @throws BadMethodStatusException 请求方法不支持
     * @throws ConflictStatusException  版本冲突异常, 当accept中包含多个Content-Type而且存在不一样的版本时抛出
     */
    Mapping lookup(String method, String URI, String accept, Comparator<String> comparator) throws NotFoundStatusException, BadMethodStatusException, ConflictStatusException;

    /**
     * 注册资源控制器
     *
     * @param controller 资源控制器
     * @return
     * @throws IllegalConfigException
     */
    Resource register(Object controller) throws IllegalConfigException;

    /**
     * 批量注册资源控制器
     *
     * @param controllers 资源控制器数组
     * @return
     * @throws IllegalConfigException
     */
    Collection<Resource> register(Object... controllers) throws IllegalConfigException;

    /**
     * 批量注册资源控制器
     *
     * @param controllers 资源控制器数组
     * @return
     * @throws IllegalConfigException
     */
    Collection<Resource> register(Collection<Object> controllers) throws IllegalConfigException;

    /**
     * 获取枚举器
     *
     * @return 映射枚举器
     */
    Enumeration<Mapping> enumeration();

}
