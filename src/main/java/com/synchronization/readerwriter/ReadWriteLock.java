package com.synchronization.readerwriter;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReadWriteLock {
    private final Lock readLock = new ReentrantLock();
    private final SharedLock writeLock = new SharedLock();
    private Integer readerCount = 0;

    void writeLock() {
        writeLock.lock();
    }

    void writeUnlock() {
        writeLock.unlock();
    }

    void readLock() {
        readLock.lock();
        readerCount++;
        System.out.println("ReadWriteLock#readLock: readerCount=" + readerCount);
        if (readerCount == 1) {
            writeLock.lock();
        }
        readLock.unlock();
    }

    void readUnlock() {
        readLock.lock();
        readerCount--;
        System.out.println("ReadWriteLock#readUnlock: readerCount=" + readerCount);
        if (readerCount == 0) {
            writeLock.unlock();
        }
        readLock.unlock();
    }
}
