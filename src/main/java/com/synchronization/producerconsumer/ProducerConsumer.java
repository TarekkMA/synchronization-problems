package com.synchronization.producerconsumer;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Here we simulate a theoretical scenario where multiple producers copies data from an input buffer into a buffer,
 * and multiple consumer that consumes that buffer and copies the result to an output buffer
 */
public class ProducerConsumer {

    public static final int DATA_SIZE = 100;
    public static final int BUFFER_SIZE = 10;

    public static void main(String[] args) {
        System.out.println("** Producer Consumer Problem **");

        ExecutorService executor = Executors.newCachedThreadPool();

        Buffer<Integer> inputBuffer = new ConcurrentCircularBuffer<>(DATA_SIZE);
        Buffer<Integer> buffer = new ConcurrentCircularBuffer<>(BUFFER_SIZE);
        Buffer<Integer> outputBuffer = new ConcurrentCircularBuffer<>(DATA_SIZE);

        // Fill input buffer with random integers
        Random r = new Random();
        for (int i = 0; i < DATA_SIZE; i++) {
            inputBuffer.put(r.nextInt(900) + 100);
        }


        System.out.print("Spawning consumer threads: ");
        for (int i = 0; i < DATA_SIZE; i++) {
            System.out.print("[" + i + "]");
            executor.execute(new Consumer<>(buffer) {
                @Override
                public void consume(Integer data) {
                    try {
                        Thread.sleep(new Random().nextInt(1000));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    outputBuffer.put(data);
                }
            });
        }
        System.out.println();

        System.out.print("Spawning producer threads: ");
        for (int i = 0; i < DATA_SIZE; i++) {
            System.out.print("[" + i + "]");
            executor.execute(new Producer<>(buffer) {
                @Override
                public Integer produce() {
                    try {
                        Thread.sleep(new Random().nextInt(1000));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return inputBuffer.take();
                }
            });
        }
        System.out.println();

        executor.shutdown();
        while (!executor.isTerminated());


        // we don't care about order
        Set<Integer> inputContent = new HashSet<>(inputBuffer.copyOfContent());
        Set<Integer> outputContent = new HashSet<>(outputBuffer.copyOfContent());
        if (inputContent.equals(outputContent)) {
            System.out.println("Transfer done");
        } else {
            System.out.println("Transfer failed");
        }
    }

}