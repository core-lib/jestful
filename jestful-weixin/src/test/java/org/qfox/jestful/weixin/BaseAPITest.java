package org.qfox.jestful.weixin;

import org.junit.After;
import org.junit.Before;
import org.qfox.jestful.client.Client;
import org.qfox.jestful.core.http.HTTP;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;

/**
 * 微信API单元测试基类
 *
 * @author 杨昌沛 646742615@qq.com
 * @date 2018-03-06 11:33
 **/
public class BaseAPITest {
    protected static final String APP_ID = "wxd0f75c5219f28a6a";
    protected static final String APP_SECRET = "3026818a2c54389e8d28b41c31268e6c";

    protected Client client;

    @Before
    public void setup() throws Exception {
        System.out.println("test started");
        client = Client.builder()
                .setProtocol("https")
                .setHostname("api.weixin.qq.com")
                .build();

        Class<? extends BaseAPITest> clazz = this.getClass();
        Field[] fields = clazz.getDeclaredFields();
        Arrays.stream(fields)
                .filter(field -> !Modifier.isFinal(field.getModifiers()))
                .filter(field -> !Modifier.isStatic(field.getModifiers()))
                .filter(field -> field.getType().isInterface())
                .filter(field -> field.getType().isAnnotationPresent(HTTP.class))
                .forEach((Field field) -> {
                    try {
                        field.setAccessible(true);
                        Class<?> interfase = field.getType();
                        Object proxy = client.create(interfase);
                        field.set(this, proxy);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                });
    }

    @After
    public void teardown() throws Exception {
        System.out.println("test completed");
    }

}
