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
        String text = "59304,59303,59315,59317,59310,59316,59318,59320,59319,59321,59323,59324,59325,59329,59322,59327,59326,59328,59333,59336,59332,59335,59338,59337,59331,59334,59343,59339,59344,59341,59340,59342,59349,59350,59345,59346,59348,59353,59347,59354,59352,59351,59356,59355,59358,59357,59360,59361,59368,59362,59363,59371,59372,59359,59373,59364,59365,59375,59367,59366,59374,59370,59369,59311,59378,59377,59379,59380,59384,59308,59385,59383,59376,59388,59389,59390,59393,59395,59396,59398,59399,59400,59401,59404,59406,59394,59407,59409,59410,59412,59415,59382,59417,59416,59414,59418,59420,59419,59421,59387,59386,59392,59391,59397,59403,59405,59307,59408,59411,59330,59413,59381,59423,59422,59426,59425,59424,59428,59427,59429,59430,59431,59432,59312,59433,59435,59436,59438,59440,59439,59437,59309,59313,59442,59443,59441,59444,59445,59447,59446,59448,59449,59314,59450,59451,59452,59456,59454,59453,59457,59402,59458,59460,59459,59462,59461,59463,59455,59464,59465,59466,59467,59469,59470,59306,59471,59472,59434,59305,59473,59468,59477,59481,59483,59480,59484,59475,59479,59486,59485,59474,59482,59487,59490,59488,59489,59491,59493,59476,59497,59492,59498,59501,59495,59502,59496,59503,59494,59478,59499";
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

        for (int i = 0; i < 1000; i++) {
            User user = userAPI.source();
            System.out.println(user);
        }
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