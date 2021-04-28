package com.synchronization.producerconsumer;

public abstract class Producer<T> implements Runnable {

    private final Buffer<T> buffer;

    protected Producer(Buffer<T> buffer) {
        this.buffer = buffer;
    }

    abstract public T produce();

    @Override
    public void run() {
        T data = produce();
        buffer.put(data);
    }
}
