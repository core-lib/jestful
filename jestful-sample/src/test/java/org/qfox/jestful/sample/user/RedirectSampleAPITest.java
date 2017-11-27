package org.qfox.jestful.sample.user;

import org.junit.Test;
import org.qfox.jestful.client.Client;
import org.qfox.jestful.client.nio.NioClient;
import org.qfox.jestful.client.redirect.Redirector;
import org.qfox.jestful.commons.Lock;
import org.qfox.jestful.commons.SimpleLock;
import org.qfox.jestful.sample.RedirectSampleAPI;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by yangchangpei on 17/9/27.
 */
public class RedirectSampleAPITest {

    @Test
    public void count() {
        String text = "61640,61606,61570,61647,61562,61635,61571,61590,61619,61597,61646,61591,61628,61654,61583,61577,61641,61605,61612,61631,61645,61593,61633,61624,61568,61617,61567,61607,61634,61625,61565,61558,61638,61560,61586,61636,61585,61644,61556,61650,61651,61630,61600,61564,61615,61613,61592,61648,61637,61581,61608,61618,61623,61618,61596,61619,61635,61596,61623,61608,61637,61581,61629,61582,61588,61651,61609,61559,61626,61609,61653,61648,61592,61630,61582,61588,61577,61629,61650,61613,61627,61644,61556,61653,61559,61627,61585,61620,61641,61620,61636,61586,61560,61632,61591,61621,61628,61638,61558,61632,61640,61621,61562,61625,61626,61565,61587,61649,61573,61583,61649,61587,61597,61646,61573,61607,61634,61603,61639,61611,61589,61576,61557,61578,61622,61594,61574,61643,61595,61601,61604,61602,61579,61580,61584,61572,61598,61616,61566,61563,61575,61599,61652,61642,61614,61561,61655,61569,61610,61647,61654,61645,61593,61603,61633,61624,61567,61590,61568,61617,61584,61570,61644,61576,61604,61650,61614,61559,61632,61636,61627,61600,61583,61584,61582,61645,61612,61572,61611,61565,61626,61619,61605,61558,61621,61613,61622,61573,61634,61654,61639,61635";
        Map<String, AtomicInteger> map = new TreeMap<>();
        String[] ports = text.split(",");
        for (String port : ports) {
            if (map.containsKey(port)) {
                map.get(port).incrementAndGet();
            } else {
                map.put(port, new AtomicInteger(1));
            }
        }
        System.out.println(map);
    }

    @Test
    public void get() throws Exception {
        NioClient client = NioClient.builder()
                .setProtocol("http")
                .setHostname("localhost")
                .setPort(8080)
                .setKeepAlive(true)
                .build();
        RedirectSampleAPI userAPI = client
                .creator()
                .addBackPlugins(new Redirector())
                .create(RedirectSampleAPI.class);

        Lock lock = new SimpleLock();
        AtomicInteger remain = new AtomicInteger(100);
        for (int i = 0; i < 100; i++) {
            int finalI = i;
            new Thread(() -> {
                try {
                    User user = userAPI.source();
                    System.out.print(finalI + ",");
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (remain.decrementAndGet() == 0) lock.openAll();
                }
            }).start();
        }
        lock.lockOne();
    }

    @Test
    public void post() throws Exception {
        RedirectSampleAPI userAPI = Client.builder()
                .setProtocol("http")
                .setHostname("localhost")
                .setPort(8080)
                .build()
                .creator()
                .addBackPlugins(new Redirector())
                .create(RedirectSampleAPI.class);

        User body = new User("Change", 2L, null, null);
        User user = userAPI.source("Programmer", body);
        System.out.println(user);

        Lock lock = new SimpleLock();
        userAPI.source("Programmer", user, (success, result, exception) -> {
            System.out.println(result);
            lock.openAll();
        });
        lock.lockOne();
    }

}