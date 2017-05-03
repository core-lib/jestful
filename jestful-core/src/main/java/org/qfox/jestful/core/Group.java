package org.qfox.jestful.core;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Description: Actor group
 * </p>
 * <p>
 * <p>
 * Company: 广州市俏狐信息科技有限公司
 * </p>
 *
 * @author Payne 646742615@qq.com
 * @date 2016年4月7日 下午12:14:34
 * @since 1.0.0
 */
public class Group implements Actor, Initialable, Destroyable {
    protected List<Actor> members = new ArrayList<Actor>();

    public Group() {
        super();
    }

    public Group(List<Actor> members) {
        super();
        this.members = members;
    }

    public Object react(Action action) throws Exception {
        action.intrude(members != null ? members : new ArrayList<Actor>());
        return action.execute();
    }

    public List<Actor> getMembers() {
        return members;
    }

    public void setMembers(List<Actor> members) {
        this.members = members;
    }

    @Override
    public void initialize(BeanContainer beanContainer) {
        for (Actor m : members) if (m instanceof Initialable) ((Initialable) m).initialize(beanContainer);
    }

    @Override
    public void destroy() {
        for (Actor m : members) if (m instanceof Destroyable) ((Destroyable) m).destroy();
    }
}
