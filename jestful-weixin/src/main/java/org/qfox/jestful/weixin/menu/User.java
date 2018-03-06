package org.qfox.jestful.weixin.menu;

/**
 * 微信公众号用户
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-03-06 16:02
 **/
public class User {
    private String user_id;

    public User() {
    }

    public User(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    @Override
    public String toString() {
        return "User{" +
                "user_id='" + user_id + '\'' +
                '}';
    }
}
