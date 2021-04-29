package com.synchronization.readerwriter;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;

/**
 * A lock that can be locked from a thread and unlocked from another
 */
public class SharedLock {
    final Semaphore semaphore = new Semaphore(1);

    void lock() {
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    void unlock() {
        semaphore.release();
    }
}
