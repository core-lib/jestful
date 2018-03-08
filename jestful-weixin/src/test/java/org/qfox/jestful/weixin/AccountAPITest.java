package org.qfox.jestful.weixin;

import org.junit.Test;
import org.qfox.jestful.weixin.customer.Account;
import org.qfox.jestful.weixin.customer.AccountAPI;
import org.qfox.jestful.weixin.customer.AccountCreateResult;

/**
 * 微信公众号客服API测试类
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-03-08 10:29
 **/
public class AccountAPITest extends TokenAPITest {
    private AccountAPI accountAPI;

    @Test
    public void create() throws Exception {
        Account account = new Account();
        account.setKf_account("646742615@qq.com");
        account.setNickname("杨昌沛");
        account.setPassword("123456");
        AccountCreateResult result = accountAPI.create(getAccessToken(), account);
        assert result.success() : result.getErrmsg();
        System.out.println(result);
    }

}
