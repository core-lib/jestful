package org.qfox.jestful.sample.user;

import org.junit.Test;
import org.qfox.jestful.commons.clock.Clock;
import org.qfox.jestful.commons.clock.LinkedClock;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class LinkedClockTests {

    @Test
    public void test() throws Exception {
        final Clock clock = new LinkedClock();
        final CountDownLatch latch = new CountDownLatch(1000);
        Random random = new Random();
        for (int i = 0; i < 1000; i++) new Thread(() -> clock.apply(latch::countDown, System.currentTimeMillis() + random.nextInt(1000))).start();
        latch.await();
        clock.destroy();
        clock.apply(() -> {
        }, System.currentTimeMillis() + 10);
    }

}
