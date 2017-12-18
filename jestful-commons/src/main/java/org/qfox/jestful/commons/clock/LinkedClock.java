package org.qfox.jestful.commons.clock;

import org.qfox.jestful.commons.Lock;
import org.qfox.jestful.commons.LockBlock;
import org.qfox.jestful.commons.SimpleLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Created by Payne on 2017/12/1.
 */
public class LinkedClock extends AbstractClock implements Clock, Runnable {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final Lock lock = new SimpleLock();
    private final Thread thread;

    private final Executor executor;
    private volatile Schedule head;
    private volatile Schedule tail;
    private volatile boolean destroyed;

    public LinkedClock() {
        this(new DefaultExecutor());
    }

    public LinkedClock(Executor executor) {
        if (executor == null) throw new NullPointerException("executor == null");
        this.executor = executor;

        this.thread = new Thread(this);
        this.thread.setDaemon(true);
    }

    @Override
    public void apply(Execution execution, long delay, TimeUnit unit) {
        if (execution == null) throw new NullPointerException("execution == null");
        if (delay < 0) throw new NegativeDelayException("delay must not less than zero");
        if (unit == null) throw new NullPointerException("time unit == null");
        long time = System.currentTimeMillis() + unit.toMillis(delay);
        final Schedule schedule = new Schedule(time, execution);
        lock.doWithLock(new LockBlock() {
            @Override
            public void execute() {
                if (destroyed) throw new IllegalStateException("destroyed");
                // 如果当前一个调度都还没有或者是已经调度过了而且当前没有等待的调度
                if (head == null) {
                    head = schedule;
                    tail = schedule;
                    // 启动线程  这里需要注意的一个地方是: 为了避免指令重排的问题 head 和 tail 变量我都用volatile来修饰 这样就能保证这两个变量的赋值并刷新回主存
                    // 一定先于 thread.start(); 指令的执行
                    // 不然的话, 由于指令重排的问题 如果thread.start(); 先执行就会有可能子线程访问head和tail的时候取到的值是 null !!
                    if (thread.isAlive()) lock.openAll(); // 线程有可能已经启动但是已经调度了所有的任务所以 head == null
                    else thread.start();
                }
                // 否则
                else {
                    // 如果该调度先于所有调度
                    if (schedule.compareTo(head) < 0) {
                        schedule.next(head);
                        head = schedule;
                        lock.openAll(); // 只有最先一个调度改变了才需要唤醒定时器
                    }
                    // 如果该调度后于所有调度或同时于最后一个调度
                    else if (schedule.compareTo(tail) >= 0) {
                        tail.next(schedule);
                        tail = schedule;
                    }
                    // 否则就是在所有调度之间
                    else {
                        // 从调度的最先一个开始遍历
                        Schedule current = head;
                        while (current.next() != null) {
                            // 如果当前遍历到的调度的下一个调度比该调度要后
                            if (current.next().compareTo(schedule) > 0) {
                                schedule.next(current.next());
                                current.next(schedule);
                                return;
                            } else {
                                current = current.next();
                            }
                        }
                        // 到最后都没有找到比该调度还要后的 那么该调度就是最后一个 不过由于上面的分支判断 理论上不会到这里来 为了更严谨就写上吧
                        current.next(schedule);
                        tail = schedule;
                    }
                }
            }
        });
    }

    @Override
    public void run() {
        while (!destroyed) {
            try {
                lock.doWithLock(new LockBlock() {
                    @Override
                    public void execute() {
                        if (head == null) {
                            lock.lockOne();
                            return;
                        }
                        long delay = head.delay();
                        if (delay <= 0) {
                            head.schedule(executor);
                            head = head.next();
                        } else {
                            lock.lockOne(delay);
                        }
                    }
                });
            } catch (Exception e) {
                logger.error("execution schedule error", e);
            } finally {
                if (destroyed) executor.destroy();
            }
        }
    }

    @Override
    public void destroy() {
        lock.doWithLock(new LockBlock() {
            @Override
            public void execute() {
                destroyed = true;
                lock.openAll();
            }
        });
    }
}
