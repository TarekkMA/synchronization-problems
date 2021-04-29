package com.synchronization.readerwriter;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ReaderWriter {

    public static final int READERS_COUNT = 10;
    public static final int WRITER_COUNT = 5;

    public static void main(String[] args) {
        System.out.println("** Reader Writer Problem **");
        ExecutorService executor = Executors.newCachedThreadPool();

        final File f = new File();
        final Random r = new Random();

        int readers = READERS_COUNT;
        int writers = WRITER_COUNT;

        while (readers > 0 && writers > 0) {
            final int freaders = readers;
            final int fwriters = writers;
            if (r.nextBoolean() && readers > 0) {
                final int fi = READERS_COUNT - freaders;
                executor.execute(() -> {
                    trySleep(r.nextInt(5000));
                    System.out.println(">> Reader #" + fi + " started");
                    System.out.println("<< Reader #" + fi + " data read -> '" + f.read() + "'");
                });
                readers--;
            } else if (writers > 0) {
                final int fi = WRITER_COUNT - fwriters;
                executor.execute(() -> {
                    trySleep(r.nextInt(1000));
                    System.out.println(">> Writer #" + fi + " started");
                    f.append("[" + fi + "]");
                    System.out.println("<< Writer #" + fi + " data written");
                });
                writers--;
            } else {
                System.err.println("Err");
            }
        }

        executor.shutdown();
        while (!executor.isTerminated()) ;
    }

    public static void trySleep(int ms) {
        try {
            Thread.sleep(new Random().nextInt(5000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static class File {
        private String content = "X";
        private final ReadWriteLock lock = new ReadWriteLock();
        private final Random r = new Random();

        String read() {
            try {
                lock.readLock();
                trySleep(r.nextInt(1000));
                return content;
            } finally {
                lock.readUnlock();
            }
        }

        void append(String txt) {
            lock.writeLock();
            trySleep(r.nextInt(1000));
            content += txt;
            lock.writeUnlock();
        }
    }
}