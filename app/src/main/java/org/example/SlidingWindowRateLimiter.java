package org.example;

import java.time.InstantSource;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SlidingWindowRateLimiter {
    private final long cfgSizeInSeconds;
    private final long cfgMaxRequestPerWindow;
    private final InstantSource instantSource;

    private AtomicLong currentWindowStart;
    private AtomicLong currentWindowCount;
    private AtomicLong prevWindowCount;

    private Lock lock = new ReentrantLock();

    private final Queue<Long> log = new ConcurrentLinkedQueue<>();

    public SlidingWindowRateLimiter(long cfgSizeInSeconds, long cfgMaxRequestPerWindow, InstantSource instantSource) {
        this.cfgSizeInSeconds = cfgSizeInSeconds;
        this.cfgMaxRequestPerWindow = cfgMaxRequestPerWindow;
        this.instantSource = instantSource;
        this.currentWindowCount = new AtomicLong(0);
        this.currentWindowStart = new AtomicLong(0);
        this.prevWindowCount = new AtomicLong(0);
    }

    public SlidingWindowRateLimiter(long cfgSizeInSeconds, long cfgMaxRequestPerWindow) {
        this(cfgSizeInSeconds, cfgMaxRequestPerWindow, InstantSource.system());
    }

    public boolean tryAcquireUsingLog() {
        long now = instantSource.instant().toEpochMilli();

        Long oldestTimestamp = log.peek(); //remove log out of window
        while (!log.isEmpty() && oldestTimestamp !=null && (now - oldestTimestamp) >= cfgSizeInSeconds) {
            log.poll();
            oldestTimestamp = log.peek();
        }

        if(log.size() < cfgMaxRequestPerWindow) {
            log.offer(now);
            return true;
        }

        return false;
    }

    public boolean tryAcquire() {
        long now = instantSource.instant().toEpochMilli();
        long timePassedInWindow = now - currentWindowStart.get();

        //check if new window and create one
        if (timePassedInWindow >= cfgSizeInSeconds) {
            //--start-critical
            try {
                lock.lock();
                currentWindowStart.set(now);
                prevWindowCount.set(currentWindowCount.get());
                currentWindowCount.set(0);
            } finally {
                lock.unlock();
            }
            //--ends-critical
            timePassedInWindow = 0;
        }

        //calculated weight
        long weightedCount = (prevWindowCount.get() * (cfgSizeInSeconds - timePassedInWindow) / cfgSizeInSeconds) + currentWindowCount.get();

        if (weightedCount < cfgMaxRequestPerWindow) {
            currentWindowCount.incrementAndGet();
            return true;
        } else {
            return false;
        }
    }
}
