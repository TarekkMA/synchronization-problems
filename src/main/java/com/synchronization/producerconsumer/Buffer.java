package com.synchronization.producerconsumer;

import java.util.List;

public interface Buffer<E> {
    E take();
    void put(E data);
    List<E> copyOfContent();
}
