package org.qfox.jestful.weixin.menu;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class MatchRule implements Serializable {
    private static final long serialVersionUID = 8814937091677051188L;

    private String group;
    private String sex;
    private String country;
    private String province;
    private String city;
    private String platform;
    private String language;

    @JsonProperty("group_id")
    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @JsonProperty("client_platform_type")
    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @Override
    public String toString() {
        return "MatchRule{" +
                "group='" + group + '\'' +
                ", sex='" + sex + '\'' +
                ", country='" + country + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", platform='" + platform + '\'' +
                ", language='" + language + '\'' +
                '}';
    }
}
