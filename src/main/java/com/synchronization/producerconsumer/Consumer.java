package com.synchronization.producerconsumer;

public abstract class Consumer<T> implements Runnable {

    private final Buffer<T> buffer;

    protected Consumer(Buffer<T> buffer) {
        this.buffer = buffer;
    }

    abstract public void consume(T data);

    @Override
    public void run() {
        T data = buffer.take();
        consume(data);
    }
}
