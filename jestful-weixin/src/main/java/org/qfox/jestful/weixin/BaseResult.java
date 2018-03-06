package org.qfox.jestful.weixin;

/**
 * 微信公众号基本结果父类
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-03-06 11:20
 **/
public class BaseResult {
    private int errcode;
    private String errmsg;

    public boolean success() {
        return errcode == 0;
    }

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    @Override
    public String toString() {
        return "BaseResult{" +
                "errcode=" + errcode +
                ", errmsg='" + errmsg + '\'' +
                '}';
    }
}
