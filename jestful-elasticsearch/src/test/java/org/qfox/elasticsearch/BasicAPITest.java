package org.qfox.elasticsearch;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.qfox.jestful.client.Client;
import org.qfox.jestful.core.http.HTTP;
import org.qfox.jestful.logging.LoggingInterceptor;

import java.lang.reflect.Field;
import java.net.URL;

/**
 * 基础API测试类
 *
 * @author Payne 646742615@qq.com
 * 2019/8/22 13:50
 */
public abstract class BasicAPITest {

    protected final ObjectMapper mapper = new ObjectMapper();

    @Before
    public void setup() throws Exception {
        Client client = Client.builder()
                .setEndpoint(new URL("http://localhost:9200"))
                .setKeepAlive(false)
                .build();
        LoggingInterceptor loggingInterceptor = new LoggingInterceptor();
        Class<? extends BasicAPITest> clazz = this.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            Class<?> type = field.getType();
            if (type.isInterface() && type.isAnnotationPresent(HTTP.class)) {
                Object value = client.creator().addBackPlugins(loggingInterceptor).create(type);
                field.setAccessible(true);
                field.set(this, value);
            }
        }
    }

    protected void print(Object obj) {
        try {
            mapper.writeValue(System.out, obj);
            System.out.println();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @After
    public void teardown() {

    }

}
