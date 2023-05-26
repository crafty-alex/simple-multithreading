package com.alex.multithreading;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.*;
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


    @Async
    public CompletableFuture<String> sendAsyncRequest(String url) {
        HttpClient httpClient = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .timeout(Duration.ofSeconds(60))
                .uri(URI.create(url))
                .build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body);
    }

    public Mono<String> sendAsyncRequestWebClient(String url) {
        WebClient webClient = WebClient.create();
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(String.class);
    }


    @Async
    public CompletableFuture<Long> calculateFactorialAsync(int number) throws InterruptedException {
        System.out.println("Calling @Async method...");
        Thread.sleep(3000);
        long factorial = calculateFactorial(number);
        return CompletableFuture.completedFuture(factorial);
    }

    public Future<Long> calculateFactorialWithExecutorService(int number) throws InterruptedException {
        System.out.println("Calling ExecutoService method...");
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        return executorService.submit(() -> calculateFactorial(number));
    }

    private long calculateFactorial(int number) {
        long factorial = 1;
        for (int i = 1; i <= number; i++) {
            factorial *= i;
        }
        return factorial;
    }

}