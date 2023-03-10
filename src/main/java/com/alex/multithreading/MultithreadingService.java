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
        System.out.println("Result 1 processing...");
        return CompletableFuture.completedFuture("Result 1");
    }

    @Async
    public CompletableFuture<String> doSomething2() {
        try {
            Thread.sleep(1000); // shorter waiting than thread 1
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Result 2 processing...");
        return CompletableFuture.completedFuture("Result 2");
    }

    @Async
    public void doSomething3() throws InterruptedException {
        semaphore.acquire();
        try {
            System.out.println("The counter is : " + counter.get());
            int threadId = counter.incrementAndGet();
            System.out.println("Thread " + threadId + " started");
            Thread.sleep(2000);
            System.out.println("Thread " + threadId + " finished");
        } finally {
            semaphore.release();
        }
    }

}