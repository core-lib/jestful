package org.qfox.jestful.server.annotation;

import java.lang.annotation.*;

/**
 * <p>
 * Description: 资源缓存控制注解, 用于控制资源的缓存属性, 例如{@code no-store}强制客户端不缓存任何信息, {@code no-cache}强制客户端每次请求都必须验证, <br/>
 * {@code public, max-age=60}表示消息可以缓存在共享位置,而且60秒内有效
 * </p>
 * <p>
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 *
 * @author Payne 646742615@qq.com
 * @date 2016年5月31日 下午4:11:39
 * @since 1.0.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Cache {

    /**
     * Cache-Control响应头的值, 例如 no-store, no-cache, public, private, max-age ...可以多个一起使用, 请参考<br/>
     * <a href="https://www.ietf.org/rfc/rfc2616.txt">RFC2616</a>
     *
     * @return
     */
    String[] value();

    /**
     * 缓存内容协商获取所需要考虑的请求头, 可以使用多个, 例如 Accept-Language, Accept-Charset, User-Agent ...
     *
     * @return
     */
    String[] vary() default {};

}
