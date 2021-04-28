package com.synchronization.producerconsumer;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConcurrentCircularBuffer<E> implements Buffer<E> {
    public final int capacity;
    private final E[] data;
    private int readHead = 0;
    private int writeHead = -1;

    private final Lock bufferLock;
    private final Semaphore emptyCountSemaphore;
    private final Semaphore dataCountSemaphore;

    public ConcurrentCircularBuffer(int capacity) {
        this.capacity = capacity;
        this.data = (E[]) new Object[capacity];
        emptyCountSemaphore = new Semaphore(capacity, true);
        dataCountSemaphore = new Semaphore(0, true);
        bufferLock = new ReentrantLock();
    }

    public void put(E element) {
        try {
            emptyCountSemaphore.acquire();
            bufferLock.lock();
            int nextWriteSeq = writeHead + 1;
            data[nextWriteSeq % capacity] = element;
            writeHead++;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bufferLock.unlock();
            dataCountSemaphore.release();
        }
    }

    public E take() {
        try {
            dataCountSemaphore.acquire();
            bufferLock.lock();
            E nextValue = data[readHead % capacity];
            readHead++;
            return nextValue;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        } finally {
            bufferLock.unlock();
            emptyCountSemaphore.release();
        }
    }

    @Override
    public List<E> copyOfContent() {
        try {
            bufferLock.lock();
            return Arrays.asList(data);
        } finally {
            bufferLock.unlock();
        }
    }
}
