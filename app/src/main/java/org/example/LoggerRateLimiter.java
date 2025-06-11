package org.example;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LoggerRateLimiter {

    private ConcurrentMap<String, Integer> oldMap;
    private ConcurrentMap<String, Integer> curMap;
    private AtomicInteger newTime;
    private final ReentrantLock lock = new ReentrantLock();


    public LoggerRateLimiter() {
        oldMap = new ConcurrentHashMap<>();
        curMap = new ConcurrentHashMap<>();
        newTime = new AtomicInteger(Integer.MIN_VALUE);
    }

    public boolean shouldPrintMessage(int timestamp, String message) {
        if(timestamp >= newTime.get() + 20) { // cleanup both
            lock.lock();
            try {
                oldMap = new ConcurrentHashMap<>();
                curMap = new ConcurrentHashMap<>();
                newTime.set(timestamp);
            }finally {
                lock.unlock();
            }

        } else if (timestamp >= newTime.get() + 10) { //crete new bucket and swap old
            lock.lock();
            try {
                oldMap = curMap;
                curMap = new ConcurrentHashMap<>();
                newTime.set(timestamp);
            }finally {
                lock.unlock();
            }
        }

        //check if present in any map
        if(curMap.containsKey(message)) {
           return false;
        }

        Integer seenAt = oldMap.getOrDefault(message, Integer.MIN_VALUE);
        if(timestamp < seenAt + 10) return false;


        curMap.put(message, timestamp);

        return true;
    }
}
