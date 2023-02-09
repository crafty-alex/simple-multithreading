package com.alex.multithreading;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.util.concurrent.Semaphore;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
@Service
public class MultithreadingService {

    private final Semaphore semaphore = new Semaphore(2);
    private final AtomicInteger counter = new AtomicInteger();

    @Async
    public CompletableFuture<String> doSomething1() {
        try {
            Thread.sleep(3000); // longer waiting thant thread 2
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return CompletableFuture.completedFuture("Result 1");
    }

    @Async
    public CompletableFuture<String> doSomething2() {
        try {
            Thread.sleep(1000); // shorter waiting than thread 1
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return CompletableFuture.completedFuture("Result 2");
    }

    @Async
    public CompletableFuture<Void> doSomething3() throws InterruptedException {
        semaphore.acquire();
        try {
            int threadId = counter.incrementAndGet();
            System.out.println("Thread " + threadId + " started");
            // do some time-consuming operation
            Thread.sleep(2000);
            System.out.println("Thread " + threadId + " finished");
        } finally {
            semaphore.release();
        }
        return CompletableFuture.completedFuture(null);
    }
}