package org.qfox.jestful.sample.user;

import java.io.Serializable;

/**
 * Created by yangchangpei on 17/10/19.
 */
public class User implements Serializable {
    private static final long serialVersionUID = 4773617196046158176L;

    private String login;
    private Long id;
    private String avatar_url;
    private String url;

    public User() {
    }

    public User(String login, Long id, String avatar_url, String url) {
        this.login = login;
        this.id = id;
        this.avatar_url = avatar_url;
        this.url = url;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("User{");
        sb.append("login='").append(login).append('\'');
        sb.append(", id=").append(id);
        sb.append(", avatar_url='").append(avatar_url).append('\'');
        sb.append(", url='").append(url).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
