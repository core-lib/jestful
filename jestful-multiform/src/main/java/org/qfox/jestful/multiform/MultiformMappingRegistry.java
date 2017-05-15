package org.qfox.jestful.multiform;

import org.qfox.jestful.core.Mapping;
import org.qfox.jestful.server.JestfulMappingRegistry;
import org.qfox.jestful.server.exception.NotFoundStatusException;

import java.util.Collection;

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
 * @date 2016年4月1日 下午3:09:22
 * @since 1.0.0
 */
public class MultiformMappingRegistry extends JestfulMappingRegistry {

    @Override
    public Collection<Mapping> lookup(String URI) throws NotFoundStatusException {
        if (URI.contains(".")) URI = URI.substring(0, URI.lastIndexOf('.'));
        return super.lookup(URI);
    }
}
